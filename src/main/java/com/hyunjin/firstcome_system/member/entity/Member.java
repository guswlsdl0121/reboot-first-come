package com.hyunjin.firstcome_system.member.entity;

import com.hyunjin.firstcome_system.auth.encryption.converter.EmailConverter;
import com.hyunjin.firstcome_system.auth.encryption.converter.PersonalInfoConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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