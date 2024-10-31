package com.hyunjin.firstcome_system.auth.session.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * FindByIndexNameSessionRepository 인터페이스의 구현체로, 메모리 내 Map을 사용하여 세션을 관리
 * 값 기반의 세션 검색(email)을 지원하기 위해 도입
 * 추후 Redis 기반의 Repsotitory로 넘어갈 것임
 */
@Component
@RequiredArgsConstructor
public class InmemoryIndexSessionRepository implements CustomSessionRepository {

    private final Map<String, MapSession> sessions;

    @Override
    public MapSession createSession() {
        MapSession session = new MapSession();
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void save(MapSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public MapSession findById(String id) {
        return sessions.get(id);
    }

    @Override
    public void deleteById(String id) {
        MapSession session = sessions.remove(id);
        if (session != null) {
            session.getAttributeNames().forEach(session::removeAttribute);
            session.setMaxInactiveInterval(Duration.ZERO);
        }
    }

    @Override
    public Map<String, MapSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return Collections.emptyMap();
        }

        return sessions.values().stream()
                .filter(session -> indexValue.equals(session.getAttribute(PRINCIPAL_NAME_INDEX_NAME)))
                .collect(Collectors.toMap(MapSession::getId, Function.identity()));
    }

    @Override
    public void deleteAllByIndex(String indexValue) {
        List<String> sessionsToRemove = sessions.values().stream()
                .filter(session -> indexValue.equals(session.getAttribute(PRINCIPAL_NAME_INDEX_NAME)))
                .map(MapSession::getId)
                .toList();

        sessionsToRemove.forEach(this::deleteById);
    }
}