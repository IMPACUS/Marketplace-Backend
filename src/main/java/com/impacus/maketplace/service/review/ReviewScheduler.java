package com.impacus.maketplace.service.review;

import com.impacus.maketplace.common.utils.LogUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReviewScheduler {
    private final ReviewService reviewService;

    public ReviewScheduler(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 삭제 후 14일이 지난 리뷰를 삭제하는 스케줄러
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    //@Scheduled(cron = "0 0 0 * * ?") // 자정
    public void cleanUpReview() {
        try {
            long result = reviewService.cleanUpReview();
            LogUtils.writeInfoLog("cleanUpReview", "[Deleted reviews] " + result);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("cleanUpReview", "Fail to delete reviews", ex);
        }
    }
}
