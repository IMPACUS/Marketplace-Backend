package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_po_product_id", columnList = "product_id"))
public class ProductOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    @Comment("색")
    private String color;

    @Column(nullable = false)
    @Comment("크기")
    private String size;

    @Column(nullable = false)
    @Comment("재고")
    private Long stock;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    @Comment("삭제 여부")
    private boolean isDeleted;

    // PostLoad 를 위해서 관리되는 데이터
    @Transient
    private String previousColor;
    @Transient
    private String previousSize;

    @PostLoad
    public void storePreviousState() {
        this.previousColor = this.color;
        this.previousSize = this.size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}
