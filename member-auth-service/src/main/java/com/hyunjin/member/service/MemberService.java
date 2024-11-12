package com.hyunjin.member.service;


import com.hyunjin.auth.service.AuthService;
import com.hyunjin.member.api.dto.request.ChangePasswordRequest;
import com.hyunjin.member.api.dto.request.SignupRequest;
import com.hyunjin.member.entity.Member;
import com.hyunjin.member.service.usecase.MemberCreator;
import com.hyunjin.member.service.usecase.MemberFinder;
import com.hyunjin.member.service.usecase.MemberUpdater;
import com.hyunjin.member.service.usecase.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberCreator memberCreator;
    private final MemberFinder memberFinder;
    private final MemberUpdater memberUpdater;
    private final MemberValidator memberValidator;

    private final AuthService authService;

    // 신규 회원 가입
    public int signUp(SignupRequest request) {
        // Step 1: 이메일 중복 확인
        memberValidator.checkEmailExists(request.getEmail());

        // Step 2: 회원 정보 저장
        Member member = memberCreator.newMember(request);
        return member.getId();
    }

    // 비밀번호 변경
    public void changePassword(Integer memberId, ChangePasswordRequest request) {
        // Step 1: 회원 정보 조회
        Member member = memberFinder.fetchById(memberId);

        // Step 2: 현재 비밀번호 검증
        memberValidator.matchPassword(request.currentPassword(), member.getPassword());

        // Step 3: 비밀번호 업데이트 및 전체 세션 로그아웃
        memberUpdater.updatePassword(member, request.newPassword());
        authService.logoutAllSessions(memberId);
    }

    // 내부 API용 id기반 사용자 조회
    public Member fetchById(Integer memberId) {
        return memberFinder.fetchById(memberId);
    }
}
