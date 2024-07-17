package com.impacus.maketplace.entity.product.history;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.MapToJsonConverter;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Table(name = "product_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_history_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false, length = 50)
    @Comment("상품명")
    private String name;

    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    @Comment("상품 이미지")
    private Map<Long, String> productImages;

    public ProductHistory(
            Long productId,
            String name, Map<Long,
            String> productImages
    ) {
        this.productId = productId;
        this.name = name;
        this.productImages = productImages;
    }

    public static ProductHistory toEntity(Product product, List<AttachFile> productImages) {
        Map<Long, String> productImagesMap = new HashMap<>();
        for (AttachFile attachFile : productImages) {
            productImagesMap.put(attachFile.getId(), attachFile.getAttachFileName());
        }

        return new ProductHistory(product.getId(), product.getName(), productImagesMap);
    }
}
