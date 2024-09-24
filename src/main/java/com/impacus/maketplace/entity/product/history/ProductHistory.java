package com.impacus.maketplace.entity.product.history;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.ListToJsonConverter;
import com.impacus.maketplace.entity.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Table(name = "product_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_history_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 50)
    @Comment("상품명")
    private String name;

    @Convert(converter = ListToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    @Comment("상품 이미지")
    private List<String> productImages;

    public ProductHistory(
            Long productId,
            String name,
            List<String> productImages
    ) {
        this.productId = productId;
        this.name = name;
        this.productImages = productImages;
    }

    public static ProductHistory toEntity(Product product) {
        return new ProductHistory(product.getId(), product.getName(), product.getProductImages());
    }

    public static ProductHistory toEntity(Long productId, String productName, List<String> productImages) {
        return new ProductHistory(productId, productName, productImages);
    }
}
