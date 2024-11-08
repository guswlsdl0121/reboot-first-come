package com.hyunjin.wishlist.client.member;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberResponse {
    private Integer id;
    private String email;
    private String name;
    private List<String> roles;
}
