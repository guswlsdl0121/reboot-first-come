package com.hyunjin.member.controller;


import com.hyunjin.common.dto.CommonResponse;
import com.hyunjin.member.dto.request.ChangePasswordRequest;
import com.hyunjin.member.dto.request.SignupRequest;
import com.hyunjin.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<String>> showPrincipal() {
        log.error("=== /me endpoint called ===");  // error 레벨로 변경

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.error("Authentication: {}", auth);

        if (auth != null) {
            log.error("Principal: {}", auth.getPrincipal());
            log.error("Authorities: {}", auth.getAuthorities());
            return ResponseEntity.ok(CommonResponse.success(auth.getName()));
        }

        log.error("No authentication found");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse.fail("인증 정보가 없습니다."));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Integer>> signup(@RequestBody @Valid SignupRequest request) {
        int memberId = memberService.signUp(request);
        return ResponseEntity.ok(CommonResponse.success("회원가입이 성공적으로 이루어졌습니다.", memberId));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody @Valid ChangePasswordRequest request) {
        Integer userId = Integer.parseInt(userDetails.getUsername());
        memberService.changePassword(userId, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경됐습니다.");
    }
}