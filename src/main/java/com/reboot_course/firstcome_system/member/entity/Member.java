package com.reboot_course.firstcome_system.member.entity;

import com.hyunjin.common.entity.BaseEntity;
import com.reboot_course.firstcome_system.auth.encryption.converter.EmailConverter;
import com.reboot_course.firstcome_system.auth.encryption.converter.PersonalInfoConverter;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    @Convert(converter = PersonalInfoConverter.class)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Convert(converter = EmailConverter.class)
    @Column(nullable = false, unique = true)
    private String email;

    @Convert(converter = PersonalInfoConverter.class)
    @Column(nullable = false)
    private String phone;

    @Convert(converter = PersonalInfoConverter.class)
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime lastPasswordUpdated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_UNVERIFIED;

    public void changePassword(String password) {
        this.password = password;
    }

    public void verifyEmail() {
        if (this.role != Role.ROLE_UNVERIFIED) {
            throw new IllegalStateException("이미 인증된 사용자입니다.");
        }
        this.role = Role.ROLE_USER;
    }
}