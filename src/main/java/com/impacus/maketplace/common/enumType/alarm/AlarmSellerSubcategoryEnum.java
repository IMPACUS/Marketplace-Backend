package com.impacus.maketplace.common.enumType.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmSellerSubcategoryEnum {
    PAYMENT_COMPLETE("결제완료", "ORDER_DELIVERY", "판매자 (결제완료)",
            "결제완료 안내\n" +
                    "\n" +
                    "#{브랜드명}님, 회원님의 주문 결제가 완료 되었어요.\n" +
                    "\n" +
                    "- 주문일: #{2024-10-05}\n" +
                    "- 주문번호: #{주문번호}\n" +
                    "- 상품명: #{상품명}\n" +
                    "- 주문금액: #{20,000}원\n" +
                    "\n" +
                    "#{하단 문구}"),
    DELIVERY_START("배송 시작", "ORDER_DELIVERY", "판매자 (배송시작)",
            "배송시작 안내\n" +
                    "\n" +
                    "#{브랜드명}님, 회원님의 배송이 시작 되었습니다.\n" +
                    "\n" +
                    "- 상품명: #{상품명}\n" +
                    "- 주문번호: #{주문번호}\n" +
                    "- 운송장번호: #{운송장번호}\n" +
                    "\n" +
                    "\n" +
                    "#{하단 문구}"),
    DELIVERY_COMPLETE("배송 완료", "ORDER_DELIVERY", "판매자 (배송완료)",
            "배송시작 안내\n" +
                    "\n" +
                    "#{브랜드명}님, 회원님의 배송이 완료 되었습니다.\n" +
                    "\n" +
                    "- 상품명: #{상품명}\n" +
                    "- 주문번호: #{주문번호}\n" +
                    "- 운송장번호: #{운송장번호}\n" +
                    "\n" +
                    "\n" +
                    "#{하단 문구}"),
    CANCEL("반품/교환/주문취소", "ORDER_DELIVERY", "판매자 (반품/교환/취소)",
            "반품/교환/취소 안내\n" +
                    "\n" +
                    "#{브랜드명}님, 회원님이 주문한 상품이 반품/교환/취소 요청이 왔습니다.\n" +
                    "\n" +
                    "- 상품명: #{상품명}\n" +
                    "- 주문번호: #{주문번호}\n" +
                    "- 운송장번호: #{운송장번호}\n" +
                    "\n" +
                    "\n" +
                    "#{하단 문구}"),
    WISH("브랜드/상품 찜", "WISH", "판매자 (찜)",
            "브랜드/상품 찜 안내\n" +
                    "\n" +
                    "#{브랜드명}님, 안녕하세요 Implace입니다.\n" +
                    "\n" +
                    "소비자 회원님께서 브랜드/상품을 찜 하였습니다!\n" +
                    "\n" +
                    "#{하단 문구}"),
    INQUIRY_REVIEW("문의/리뷰", "INQUIRY_REVIEW", "판매자 (문의 리뷰)",
            "문의/리뷰 안내\n" +
                    "\n" +
                    "#{브랜드명}님, 안녕하세요 Implace입니다.\n" +
                    "\n" +
                    "소비자 회원님께서 소중한 문의/리뷰를 남겼습니다.\n" +
                    "\n" +
                    "\n" +
                    "#{하단 문구}"),
    //    ADVERTISEMENT("광고", "ADVERTISEMENT"),
    OPEN_APPROVAL("입점 승인", "OPEN", "판매자 (입점승인)",
            "입점승인 안내 \n" +
                    "\n" +
                    "#{브랜드명}님, 안녕하세요 Implace입니다.\n" +
                    "\n" +
                    "Implace 마켓플레이스의 신청하신 입점이 승인되었습니다!\n" +
                    "\n" +
                    "Implace 서비스를 이용해 주셔서 감사합니다. \n" +
                    "\n" +
                    "판매자 페이지 로그인 하셔서 상품을 등록해주세요~\n" +
                    "\n" +
                    "다양한 마케팅, 홍보 서비스 제공해 드리오니 해당 내용으로 담당자가 신청하신 번호로 연락 드릴 예정입니다.\n" +
                    "\n" +
                    "\n" +
                    "#{하단 문구}"),
    OPEN_REJECTION("입점 거절", "OPEN", "판매자 (입점반려)",
            "입점반려 안내 \n" +
                    "\n" +
                    "#{브랜드명}님, 안녕하세요 Implace입니다.\n" +
                    "\n" +
                    "Implace 마켓플레이스의 신청하신 입점이 반려되었습니다.\n" +
                    "\n" +
                    "사유는 담당자님께서 직접 지원 신청해주신 번호/메일로 연락 드릴 예정입니다.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "#{하단 문구}");
    private String value;
    private String category;
    private String kakaoCode;
    private String template;
}
