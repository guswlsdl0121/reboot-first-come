package com.hyunjin.member.service;


import com.hyunjin.auth.core.service.AuthService;
import com.hyunjin.entity.Member;
import com.hyunjin.member.dto.request.ChangePasswordRequest;
import com.hyunjin.member.dto.request.SignupRequest;
import com.hyunjin.member.usecase.MemberAppender;
import com.hyunjin.member.usecase.MemberFinder;
import com.hyunjin.member.usecase.MemberUpdater;
import com.hyunjin.member.usecase.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberValidator memberValidator;
    private final MemberAppender memberAppender;
    private final MemberFinder memberFinder;
    private final MemberUpdater memberUpdater;

    private final AuthService authService;

    public int signUp(SignupRequest request) {
        memberValidator.checkEmailExists(request.getEmail());
        Member member = memberAppender.newMember(request);

        return member.getId();
    }

    public void changePassword(Integer memberId, ChangePasswordRequest request) {
        Member member = memberFinder.fetchById(memberId);

        memberValidator.matchPassword(request.currentPassword(), member.getPassword());
        memberUpdater.updatePassword(member, request.newPassword());

        authService.logoutAllSessions(memberId);
    }
}