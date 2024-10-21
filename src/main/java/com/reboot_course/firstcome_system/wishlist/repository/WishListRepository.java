package com.reboot_course.firstcome_system.wishlist.repository;

import com.reboot_course.firstcome_system.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<Wishlist, Integer> {
}
