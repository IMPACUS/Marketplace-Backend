package com.impacus.maketplace.entity.product;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.impacus.maketplace.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE shopping_basket SET is_deleted = true WHERE shopping_basket_id = ?")
@Where(clause = "is_deleted = false")
public class ShoppingBasket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_basket_id")
    private Long id;

    @Column(nullable = false)
    private Long productOptionId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long quantity;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    public ShoppingBasket(Long productOptionId, Long quantity, Long userId) {
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
