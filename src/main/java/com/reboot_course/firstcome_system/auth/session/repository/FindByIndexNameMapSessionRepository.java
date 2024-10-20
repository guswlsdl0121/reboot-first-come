package com.reboot_course.firstcome_system.auth.session.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FindByIndexNameMapSessionRepository implements FindByIndexNameSessionRepository<MapSession> {

    private final Map<String, MapSession> sessions;

    @Override
    public MapSession createSession() {
        MapSession result = new MapSession();
        sessions.put(result.getId(), result);
        return result;
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
        sessions.remove(id);
    }

    @Override
    public Map<String, MapSession> findByIndexNameAndIndexValue(String indexName, String indexValue) {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return java.util.Collections.emptyMap();
        }
        Map<String, MapSession> result = new java.util.HashMap<>();
        for (MapSession session : sessions.values()) {
            String principalName = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);
            if (indexValue.equals(principalName)) {
                result.put(session.getId(), session);
            }
        }
        return result;
    }
}
