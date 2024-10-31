package com.hyunjin.firstcome_system.wishlist.repository;

import com.hyunjin.firstcome_system.member.entity.Member;
import com.hyunjin.firstcome_system.product.entity.Product;
import com.hyunjin.firstcome_system.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<Wishlist, Integer> {
    boolean existsByMemberAndProduct(Member member, Product product);

    @Query("""
            SELECT w.id
            FROM Wishlist w
            WHERE w.member.id = :memberId AND w.id < :cursor
            ORDER BY w.id DESC
            LIMIT :size
            """)
    List<Integer> getWishlistIds(@Param("memberId") Integer memberId,
                                 @Param("cursor") Integer cursor,
                                 @Param("size") Integer size);

    @Query("""
            SELECT w
            FROM Wishlist w
            JOIN FETCH w.product
            WHERE w.id IN :ids
            """)
    List<Wishlist> findByIdsWithProduct(@Param("ids") List<Integer> ids);
}
