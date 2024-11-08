package com.hyunjin.member.api.dto.response;

import com.hyunjin.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class MemberResponse {
    private Integer id;
    private String email;
    private String name;
    private List<String> roles;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .roles(Collections.singletonList(member.getRole().name()))
                .build();
    }
}
