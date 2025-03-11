package com.impacus.maketplace.dto.review;

import com.impacus.maketplace.common.enumType.searchCondition.QnAReviewSearchCondition;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
public class QnaReviewSearchCondition {
    private Pageable pageable;
    private String detailKeyword;
    private String keyword;
    private LocalDate startAt;
    private LocalDate endAt;
    private QnAReviewSearchCondition searchCondition;

    public QnaReviewSearchCondition(
            Pageable pageable,
            String detailKeyword,
            String keyword,
            LocalDate startAt,
            LocalDate endAt,
            QnAReviewSearchCondition searchCondition
    ) {
        this.pageable = pageable;
        this.detailKeyword = detailKeyword;
        this.keyword = keyword;
        this.startAt = startAt;
        this.endAt = endAt;
        this.searchCondition = searchCondition;
    }

    public static QnaReviewSearchCondition toDTO(Pageable pageable,
                                                 String detailKeyword,
                                                 String keyword,
                                                 LocalDate startAt,
                                                 LocalDate endAt,
                                                 QnAReviewSearchCondition searchCondition) {
        return new QnaReviewSearchCondition(pageable, detailKeyword, keyword, startAt, endAt, searchCondition);
    }

}
