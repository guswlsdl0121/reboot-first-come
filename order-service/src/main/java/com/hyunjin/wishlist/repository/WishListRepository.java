package com.hyunjin.wishlist.repository;

import com.hyunjin.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<Wishlist, Integer> {
    boolean existsByMemberIdAndProductId(Integer memberId, Integer productId);

    @Query("""
                SELECT w.id
                FROM Wishlist w
                WHERE w.memberId = :memberId AND w.id < :cursor
                ORDER BY w.id DESC
                LIMIT :size
            """)
    List<Integer> getWishlistIds(
            @Param("memberId") Integer memberId,
            @Param("cursor") Integer cursor,
            @Param("size") Integer size
    );

    @Query("""
                SELECT w
                FROM Wishlist w
                WHERE w.id IN :ids
            """)
    List<Wishlist> findByIds(@Param("ids") List<Integer> ids);
}