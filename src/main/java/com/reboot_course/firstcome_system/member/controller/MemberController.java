package com.reboot_course.firstcome_system.member.controller;

import com.hyunjin.common.dto.CommonResponse;
import com.reboot_course.firstcome_system.member.dto.request.ChangePasswordRequest;
import com.reboot_course.firstcome_system.member.dto.request.SignupRequest;
import com.reboot_course.firstcome_system.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController implements MemberWebAPI {
    private final MemberService memberService;

    @Override
    @GetMapping("/me")
    public CommonResponse<String> showPrincipal(@AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("로그인에 성공했습니다!", userDetails.getUsername());
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Integer>> signup(@RequestBody @Valid SignupRequest request) {
        int memberId = memberService.signUp(request);
        return ResponseEntity.ok(CommonResponse.success("회원가입이 성공적으로 이루어졌습니다.", memberId));
    }

    @Override
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody @Valid ChangePasswordRequest request) {
        Integer userId = Integer.parseInt(userDetails.getUsername());
        memberService.changePassword(userId, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경됐습니다.");
    }
}