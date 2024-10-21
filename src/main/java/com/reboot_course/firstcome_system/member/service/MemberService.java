package com.reboot_course.firstcome_system.member.service;

import com.reboot_course.firstcome_system.auth.core.service.AuthService;
import com.reboot_course.firstcome_system.member.dto.request.ChangePasswordRequest;
import com.reboot_course.firstcome_system.member.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.member.usecase.MemberAppender;
import com.reboot_course.firstcome_system.member.usecase.MemberFinder;
import com.reboot_course.firstcome_system.member.usecase.MemberUpdater;
import com.reboot_course.firstcome_system.member.usecase.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        Member member = memberFinder.fetchByEmail(email);

        memberValidator.matchPassword(member.getPassword(), request.currentPassword());
        memberUpdater.updatePassword(member, request.newPassword());

        authService.logoutAllSessions(email);
    }
}