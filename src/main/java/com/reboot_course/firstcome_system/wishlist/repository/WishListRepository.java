package com.reboot_course.firstcome_system.wishlist.repository;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.product.entity.Product;
import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<Wishlist, Integer> {
    boolean existsByMemberAndProduct(Member member, Product product);
}
