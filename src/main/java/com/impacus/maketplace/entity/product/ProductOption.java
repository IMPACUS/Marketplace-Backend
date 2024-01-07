package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE product_option SET is_deleted = true WHERE product_option_id = ?")
@Where(clause = "is_deleted = false")
public class ProductOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    private String color; // 색

    private String size; // 크기

    @Column(nullable = false)
    private Long stock; // 재고

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부
}
