package com.reboot_course.firstcome_system.member.controller;

import com.reboot_course.firstcome_system.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.member.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public CommonResponse<String> login(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("{}", SecurityContextHolder.getContext());
        return CommonResponse.success("로그인에 성공했습니다!", userDetails.getUsername());
    }


    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Integer>> signup(@RequestBody @Valid SignupRequest request) {
        int memberId = memberService.signUp(request);
        return ResponseEntity.ok(CommonResponse.success("User registered successfully", memberId));
    }
}
