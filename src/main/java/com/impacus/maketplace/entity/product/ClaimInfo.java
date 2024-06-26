package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@MappedSuperclass
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ClaimInfo extends BaseEntity {
    @Column(nullable = false)
    @Comment("상품 회수 목록")
    private String recallInfo;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("클레임 비용")
    private String claimCost;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("클레임 정책 안내")
    private String claimPolicyGuild;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("클레임 문의처")
    private String claimContactInfo;
}
