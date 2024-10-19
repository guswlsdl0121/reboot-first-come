package com.reboot_course.firstcome_system.member.repository;

import com.reboot_course.firstcome_system.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
}
