package com.impacus.maketplace.entity.review;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "seller_Id")
    private Long sellerId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "buyer_contents")
    private String buyerContents;

    @Column(name = "buyer_upload_img_id")
    private Long buyerUploadImgId;

    @Column(name = "seller_comment")
    private String sellerComment;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @Column(name = "is_archive")
    private Boolean isArchive;

    @Column(name = "archive_at")
    private ZonedDateTime archiveAt;
}
