package com.impacus.maketplace.common.enumType.point;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum RewardPointType {
    CHECK(1, "출석체크", "00시 기준 로그인시 지급", 30L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    REVIEW(2, "리뷰 작성", "리뷰 작성시 지급", null, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SHARE_APP(3, "앱 공유", "초대 받은 유저와 같이 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SHARE_PRODUCT(4, "상품 공유", "초대 받은 유저와 같이 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SNS_TAG(5, "SNS 태그", "SNS 태그 시 지급", 1000L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    PURCHASE_PRODUCT(6, "상품 구매", "그린태그 상품 구매시 지급", null, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    COUPON(7, "쿠폰 포인트 지급", "쿠폰 사용시 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    ADMIN_PROVIDE(8, "관리자에 의한 포인트 지급", "관리자에 의한 포인트 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    UPGRADE_LEVEL(9, "사용자 레벨 상승", "사용자 레벨 상승시 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),

    // Dummy data
    PROMOTION_EVENT_1(10, "프로모션 이벤트 참여", "이벤트 참여시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PROMOTION_EVENT_2(11, "프로모션 이벤트 완료", "이벤트 완료시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    RECOMMENDATION_BONUS_1(12, "추천인 보너스 1", "추천 1명 완료 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    RECOMMENDATION_BONUS_2(13, "추천인 보너스 2", "추천 2명 완료 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    RECOMMENDATION_BONUS_3(14, "추천인 보너스 3", "추천 3명 완료 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    RECOMMENDATION_BONUS_4(15, "추천인 보너스 4", "추천 4명 완료 시 지급", 400L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    SOCIAL_SHARE_FACEBOOK(16, "페이스북 공유", "페이스북에 공유 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SOCIAL_SHARE_INSTAGRAM(17, "인스타그램 공유", "인스타그램에 공유 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SOCIAL_SHARE_TWITTER(18, "트위터 공유", "트위터에 공유 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SOCIAL_SHARE_LINE(19, "라인 공유", "라인에 공유 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    BIRTHDAY_BONUS(20, "생일 보너스", "생일에 지급", 1000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    ANNIVERSARY_BONUS(21, "가입 기념 보너스", "가입 기념일에 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    LOYALTY_BONUS_1_YEAR(22, "1년 회원 보너스", "1년 가입 유지 시 지급", 2000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    LOYALTY_BONUS_2_YEARS(23, "2년 회원 보너스", "2년 가입 유지 시 지급", 3000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SURVEY_PARTICIPATION_1(24, "설문 참여 1", "간단 설문 참여시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SURVEY_PARTICIPATION_2(25, "설문 참여 2", "상세 설문 참여시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SURVEY_COMPLETION(26, "설문 완료", "설문 완료 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    APP_UPDATE_INSTALL(27, "앱 업데이트 설치", "최신 버전 설치 시 지급", 150L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    FEEDBACK_CONTRIBUTION(28, "피드백 제공", "피드백 제출 시 지급", 250L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PURCHASE_DISCOUNTED_ITEM(29, "할인 상품 구매", "할인 상품 구매 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PURCHASE_NEW_ITEM(30, "신상품 구매", "신상품 구매 시 지급", 120L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    LIMITED_EDITION_PURCHASE(31, "한정판 구매", "한정판 구매 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    FIRST_PURCHASE(32, "첫 구매", "첫 구매 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    RETURNING_USER(33, "재방문 보너스", "재방문 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    APP_SHARE_LINK(34, "앱 링크 공유", "앱 링크 공유 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    WEEKLY_LOGIN_BONUS(35, "주간 로그인 보너스", "매주 로그인 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    MONTHLY_LOGIN_BONUS(36, "월간 로그인 보너스", "매월 로그인 시 지급", 1000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    COMMENT_CONTRIBUTION(37, "댓글 기여", "댓글 작성 시 지급", 10L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    TOP_REVIEW(38, "베스트 리뷰", "우수 리뷰 선정 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    COMMUNITY_POST_CONTRIBUTION(39, "커뮤니티 게시물 작성", "커뮤니티 게시물 작성 시 지급", 20L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    REDEEM_REWARD(40, "리워드 교환", "리워드 포인트 교환 시 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    WISHLIST_ADDITION(41, "위시리스트 추가", "위시리스트에 추가 시 지급", 10L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    ORDER_OVER_AMOUNT(42, "일정 금액 이상 구매", "특정 금액 이상 구매 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    CUSTOMER_SERVICE_FEEDBACK(43, "고객 서비스 피드백", "고객 서비스 피드백 제공 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    PRODUCT_FEEDBACK(44, "제품 피드백", "제품 피드백 제공 시 지급", 150L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    DISCOUNT_VOUCHER_REDEMPTION(45, "할인 바우처 사용", "할인 바우처 사용 시 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    SPECIAL_PROMOTION(46, "특별 프로모션", "특별 이벤트 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    EXCLUSIVE_OFFER_REDEMPTION(47, "독점 혜택 교환", "독점 혜택 사용 시 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    RETURN_CUSTOMER_BONUS(48, "재구매 보너스", "재구매 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    USER_SURVEY_COMPLETION(49, "사용자 설문 완료", "사용자 설문 완료 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    MARKET_RESEARCH_PARTICIPATION(50, "시장 조사 참여", "시장 조사 참여 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    REFERRAL_BONUS_NEW_USER(51, "신규 유저 추천", "신규 유저 추천시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PRODUCT_VIDEO_REVIEW(52, "상품 동영상 리뷰", "동영상 리뷰 작성시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    QUICK_SURVEY_COMPLETION(53, "퀵 서베이 완료", "짧은 설문 완료시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    FEEDBACK_SURVEY_COMPLETION(54, "피드백 설문 완료", "피드백 설문 완료시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    NEW_FEATURE_TRIAL(55, "신규 기능 사용", "신규 기능 사용시 지급", 150L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SOCIAL_SHARE_WECHAT(56, "위챗 공유", "위챗에 공유시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SOCIAL_SHARE_WHATSAPP(57, "왓츠앱 공유", "왓츠앱에 공유시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    COMPLETE_PROFILE(58, "프로필 완성", "프로필 작성 완료시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    DAILY_LOG_IN(59, "일일 로그인 보너스", "매일 로그인시 지급", 10L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    MONTHLY_SURVEY_PARTICIPATION(60, "월간 설문 참여", "월간 설문 참여시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    VIP_MEMBERSHIP_BONUS(61, "VIP 멤버십 보너스", "VIP 멤버십 사용자 지급", 1000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PRODUCT_TESTING(62, "제품 테스트 참여", "제품 테스트 후 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    ORDER_CONFIRMATION(63, "주문 확정 보너스", "주문 확정 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    FIRST_REVIEW_BONUS(64, "첫 리뷰 보너스", "첫 리뷰 작성 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    MULTI_PURCHASE_BONUS(65, "다중 구매 보너스", "특정 횟수 이상 구매시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SEASONAL_CAMPAIGN(66, "시즌 캠페인 보너스", "특정 시즌 캠페인 참여 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    PRODUCT_RATING(67, "상품 평점", "상품 평점 작성 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SERVICE_RATING(68, "서비스 평점", "서비스 평점 작성 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PROMOTION_REFERRAL(69, "프로모션 추천", "프로모션 추천 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    GIFT_REDEMPTION(70, "선물 교환", "포인트 선물 교환 시 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    NEWSLETTER_SIGNUP(71, "뉴스레터 가입", "뉴스레터 구독 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    FEEDBACK_REWARD(72, "의견 제출", "의견 제출 시 지급", 250L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    BUG_REPORT_REWARD(73, "버그 신고 보상", "버그 신고 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    IN_APP_PURCHASE(74, "앱 내 구매 보너스", "앱 내 상품 구매시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    FESTIVAL_EVENT(75, "축제 이벤트", "특별 축제 이벤트 참여 시 지급", 600L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    DAILY_CHALLENGE_COMPLETE(76, "일일 챌린지 완료", "일일 챌린지 완료 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    WEEKLY_CHALLENGE_COMPLETE(77, "주간 챌린지 완료", "주간 챌린지 완료 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    MONTHLY_CHALLENGE_COMPLETE(78, "월간 챌린지 완료", "월간 챌린지 완료 시 지급", 1000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    JOINING_EVENT(79, "가입 이벤트 참여", "가입 시 특별 이벤트 참여 시 지급", 150L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    COMPLETING_EVENT(80, "이벤트 완료", "이벤트 완료 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    LONG_TERM_MEMBER(81, "장기 회원", "3년 이상 회원 유지시 지급", 3000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PRODUCT_REVIEW_WITH_IMAGE(82, "이미지 포함 리뷰 작성", "이미지 포함 리뷰 작성 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    PRODUCT_REVIEW_WITH_VIDEO(83, "비디오 포함 리뷰 작성", "비디오 포함 리뷰 작성 시 지급", 400L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    EVENT_SHARE_ON_SOCIAL_MEDIA(84, "소셜 미디어 이벤트 공유", "소셜 미디어에 이벤트 공유 시 지급", 150L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SPECIAL_DAY_LOGIN(85, "특별 기념일 로그인", "특정 기념일에 로그인 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    UNIQUE_PURCHASE_BONUS(86, "특수 상품 구매", "특수 상품 구매 시 지급", 250L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    COMPLETE_ONBOARDING(87, "온보딩 완료", "온보딩 절차 완료 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    DAILY_INTERACTION(88, "일일 상호작용 보너스", "매일 활동 시 지급", 20L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    WEEKLY_INTERACTION(89, "주간 상호작용 보너스", "매주 활동 시 지급", 70L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PRODUCT_SURVEY_COMPLETION(90, "제품 설문 완료", "제품 관련 설문 완료 시 지급", 300L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    SPECIAL_SEASON_BONUS(91, "특별 시즌 보너스", "특정 시즌에 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    APP_INSTALL_BONUS(92, "앱 설치 보너스", "앱 설치 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PURCHASE_MILESTONE(93, "구매 마일스톤", "특정 구매 횟수 달성 시 지급", 500L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    LOYAL_CUSTOMER_BONUS(94, "충성 고객 보너스", "특정 기간 이상 이용시 지급", 1000L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    RETURN_ITEM_BONUS(95, "반품 아이템 보너스", "반품 후 재구매 시 지급", 100L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    PRODUCT_WISHLIST_ADDITION(96, "위시리스트 추가 보너스", "상품 위시리스트 추가 시 지급", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PRODUCT_RECOMMENDATION(97, "상품 추천", "상품 추천 시 지급", 150L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    WEEKEND_PURCHASE_BONUS(98, "주말 구매 보너스", "주말 구매 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    LATE_NIGHT_PURCHASE(99, "야간 구매", "밤 10시 이후 구매 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    DAWN_PURCHASE(100, "새벽 구매", "밤 4시 이전 구매 시 지급", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO);


    private final int code;
    private final String value;
    private final String issueCondition;
    private final Long allocatedPoints; // 지급 포인트가 고정이 아닌 경우 null
    private final Duration expirationPeriod;
    private final GrantMethod grantMethod;

    public static List<RewardPointType> getManualRewardPointTypes() {
        List<RewardPointType> result = new ArrayList<>();
        for (RewardPointType rewardPointType : RewardPointType.values()) {
            if (rewardPointType.getGrantMethod() == GrantMethod.MANUAL) {
                result.add(rewardPointType);
            }
        }

        return result;
    }

    /**
     * 지급 조건 검색어가 존재하는지 확인하는 함수
     *
     * @param path
     * @param keyword
     * @return
     */
    public static BooleanExpression containsEnumValue(EnumPath<RewardPointType> path, String keyword) {
        List<RewardPointType> types = new ArrayList<>();

        for (RewardPointType type : RewardPointType.values()) {
            if (type.getIssueCondition().contains(keyword)) {
                types.add(type);
            }
        }

        return path.in(types);
    }
}
