package com.hyunjin.member.api.controller;


import com.hyunjin.common.dto.CommonResponse;
import com.hyunjin.member.api.dto.request.ChangePasswordRequest;
import com.hyunjin.member.api.dto.request.SignupRequest;
import com.hyunjin.member.api.dto.response.MemberResponse;
import com.hyunjin.member.entity.Member;
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
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<String>> showPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return ResponseEntity.ok(CommonResponse.success(auth.getName()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonResponse.fail("인증 정보가 없습니다."));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Integer>> signup(@RequestBody @Valid SignupRequest request) {
        int memberId = memberService.signUp(request);
        return ResponseEntity.ok(CommonResponse.success("회원가입이 성공적으로 이루어졌습니다.", memberId));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer memberId = Integer.parseInt(auth.getName());
        memberService.changePassword(memberId, request);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경됐습니다.");
    }

    @GetMapping("/internal/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Integer memberId) {
        Member member = memberService.fetchById(memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}