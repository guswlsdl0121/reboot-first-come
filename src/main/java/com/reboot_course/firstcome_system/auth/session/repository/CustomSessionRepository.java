package com.reboot_course.firstcome_system.auth.session.repository;

import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;

/**
 * FindByIndexNameSessionRepository를 확장한 커스텀 세션 리포지토리 인터페이스
 * 요구사항중 특정 사용자의 모든 세션 초기화 구현을 위해 따로 FindByIndexNameSessionRepository를 랩핑
 */
public interface CustomSessionRepository extends FindByIndexNameSessionRepository<MapSession> {
    void deleteAllByIndex(String indexValue);
}
