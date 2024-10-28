package com.reboot_course.firstcome_system.wishlist.entity;

import com.reboot_course.firstcome_system.member.entity.Member;
import com.reboot_course.firstcome_system.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"}))
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    public Integer getProductId() {
        return product.getId();
    }

    public String getProductName() {
        return product.getName();
    }

    public BigDecimal getProductPrice() {
        return product.getPrice();
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        } else {
            throw new IllegalStateException("수량은 1 미만으로 감소할 수 없습니다.");
        }
    }
}