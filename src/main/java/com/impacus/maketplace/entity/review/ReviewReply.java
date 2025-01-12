package com.impacus.maketplace.entity.review;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "review_reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_reply_id")
    private Long id;

    @Column(name = "review_id", nullable = false, unique = true)
    private Long reviewId;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    public ReviewReply(
            Long reviewId,
            String contents
    ) {
        this.reviewId = reviewId;
        this.contents = contents;
    }
}
