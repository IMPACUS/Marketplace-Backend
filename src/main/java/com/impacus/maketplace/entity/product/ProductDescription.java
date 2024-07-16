package com.impacus.maketplace.entity.product;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE product_description SET is_deleted = true WHERE product_description_id = ?")
@Where(clause = "is_deleted = false")
public class ProductDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_description_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("상품 설명")
    private String description;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    public void setDescription(String description) {
        this.description = description;
    }
}
