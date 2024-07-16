package com.impacus.maketplace.entity.product.history;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "product_option_history")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_history_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productOptionId;

    @Column(nullable = false)
    @Comment("색")
    private String color; // 색

    @Column(nullable = false)
    @Comment("크기")
    private String size; // 크기
}
