package com.reboot_course.firstcome_system.member.controller;

import com.reboot_course.firstcome_system.auth.session.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.common.CommonResponse;
import com.reboot_course.firstcome_system.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Integer>> signup(@RequestBody @Valid SignupRequest request) {
        int memberId = memberService.signUp(request);
        return ResponseEntity.ok(CommonResponse.success("User registered successfully", memberId));
    }
}
