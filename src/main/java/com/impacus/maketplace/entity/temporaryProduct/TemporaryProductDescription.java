package com.impacus.maketplace.entity.temporaryProduct;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryProductDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_product_description_id")
    private Long id;

    @Column(nullable = false)
    private Long temporaryProductId;

    @Column(columnDefinition = "TEXT")
    private String description; // 상품 설명

    public void setDescription(String description) {
        this.description = description;
    }
}
