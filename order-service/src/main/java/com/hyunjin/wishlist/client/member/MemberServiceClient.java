package com.hyunjin.wishlist.client.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-auth-service")
public interface MemberServiceClient {
    @GetMapping("/api/v1/member/internal/{memberId}")
    MemberResponse getMember(@PathVariable Integer memberId);
}