package com.impacus.maketplace.config;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForSellerRepository;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {
    private final AlarmAdminForUserRepository alarmAdminForUserRepository;
    private final AlarmAdminForSellerRepository alarmAdminForSellerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initializeUserAlarm();
        this.initializeSellerAlarm();
    }

    private void initializeUserAlarm() {
        List<AlarmAdminForUser> all = alarmAdminForUserRepository.findAll();
        List<AlarmAdminForUser> list = new ArrayList<>();

        Optional<AlarmAdminForUser> optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("PAYMENT_COMPLETE")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.ORDER_DELIVERY, AlarmUserSubcategoryEnum.PAYMENT_COMPLETE,
                    "제품별 상품 준비기간은 1-2일 정도 소요될 수 있으며,\n추가지연 되는 경우 문자 또는 전화로 안내드립니다.",
                    "", AlarmUserSubcategoryEnum.PAYMENT_COMPLETE.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("DELIVERY_START")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.ORDER_DELIVERY, AlarmUserSubcategoryEnum.DELIVERY_START,
                    "제품별 상품 준비기간은 1-2일 정도 소요될 수 있으며,\n추가지연 되는 경우 문자 또는 전화로 안내드립니다.",
                    "", AlarmUserSubcategoryEnum.DELIVERY_START.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("DELIVERY_COMPLETE")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.ORDER_DELIVERY, AlarmUserSubcategoryEnum.DELIVERY_COMPLETE,
                    "저희 서비스를 이용해 주셔서 감사합니다!\n앞으로 더 좋은 서비스 유지하기 위해 노력하겠습니다! :)",
                    "", AlarmUserSubcategoryEnum.DELIVERY_COMPLETE.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("CANCEL")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.ORDER_DELIVERY, AlarmUserSubcategoryEnum.CANCEL,
                    "반품 요청을 하셨을 경우 발생하는 배송비는 환불되는 금액에서 차감되어 \n" +
                            "환불 처리 되며 교환 요청을 하셨을 경우 발생하는 \n" +
                            "왕복 배송비 5,000원은 국민은행\n" +
                            "29250101398977 김성현 (임팩커스) 계좌로 입금해주시면 됩니다.\n" +
                            "\n" +
                            "택배 기사님 방문수거 요청시 1~2일 이내로 수거가 진행 됩니다.\n" +
                            "\n" +
                            "* 교환 or 반품시 가급적 하나의 택배 봉투에 함께 이중 포장하여 발송 부탁드립니다.\n" +
                            "\n" +
                            "택배 봉투 하나당 택배 비용이 발생하기 때문에 추가 금액 발생시 고객님께서 부담 해주셔야 하는 점 양해 부탁드립니다.\n" +
                            "\n" +
                            "이외 다른 문의 사항이 있으시면 아임플레이스 고객센터(070-8064-8890) 문의주시기 바랍니다. \n" +
                            "감사합니다.",
                    "", AlarmUserSubcategoryEnum.CANCEL.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("RESTOCK") && a.getSubcategory().name().equals("RESTOCK")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.RESTOCK, AlarmUserSubcategoryEnum.RESTOCK,
                    "입고 수량이 한정되어 있으므로, 구매 시점에 따라 품절이 발생할 수 있습니다.\n" +
                            "\n" +
                            "추가지연이 발생하는 경우, 문자 또는 전화로 안내드립니다.",
                    "", AlarmUserSubcategoryEnum.RESTOCK.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("REVIEW") && a.getSubcategory().name().equals("REVIEW")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.REVIEW, AlarmUserSubcategoryEnum.REVIEW,
                    "감사합니다.", "", AlarmUserSubcategoryEnum.REVIEW.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("REVIEW") && a.getSubcategory().name().equals("SERVICE_EVALUATION")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.REVIEW, AlarmUserSubcategoryEnum.SERVICE_EVALUATION,
                    "감사합니다.", "", AlarmUserSubcategoryEnum.SERVICE_EVALUATION.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("SERVICE_CENTER") && a.getSubcategory().name().equals("SERVICE_CENTER")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.SERVICE_CENTER, AlarmUserSubcategoryEnum.SERVICE_CENTER,
                    "문의 내역 확인은 아래와 같습니다.\n" +
                            "\n" +
                            "앱 > 아임페이지 > 고객센터",
                    "", AlarmUserSubcategoryEnum.SERVICE_CENTER.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("SHOPPING_BENEFITS") && a.getSubcategory().name().equals("COUPON_EXTINCTION_1")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.SHOPPING_BENEFITS, AlarmUserSubcategoryEnum.COUPON_EXTINCTION_1,
                    "※ 이 메시지는 이용약관(계약서) 동의에 따라 지급된\n" +
                            " 쿠폰 대한 소멸 예정 안내 메세지입니다.\n" +
                            "\n" +
                            "※ 문자 발송 시점에 따라 쿠폰에 차이가 있을 수 있으며, \n" +
                            "쿠폰 유효기간 및 이용 안내는 아래 쿠폰 조회에서\n" +
                            " 확인하실 수 있습니다.",
                    "*해당 알림은 고객님이 보유중인 쿠폰 만료 \n" +
                            "30일전에 발송됩니다.",
                    AlarmUserSubcategoryEnum.COUPON_EXTINCTION_1.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("SHOPPING_BENEFITS") && a.getSubcategory().name().equals("COUPON_EXTINCTION_2")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.SHOPPING_BENEFITS,
                    AlarmUserSubcategoryEnum.COUPON_EXTINCTION_2,
                    "※ 이 메시지는 이용약관(계약서) 동의에 따라 지급된\n" +
                            " 쿠폰 대한 소멸 예정 안내 메세지입니다.\n" +
                            "\n" +
                            "※ 문자 발송 시점에 따라 쿠폰에 차이가 있을 수 있으며, \n" +
                            "쿠폰 유효기간 및 이용 안내는 아래 쿠폰 조회에서\n" +
                            " 확인하실 수 있습니다.",
                    "*해당 알림은 고객님이 보유중인 쿠폰 만료 \n" +
                            "1일전에 발송됩니다.",
                    AlarmUserSubcategoryEnum.COUPON_EXTINCTION_2.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("SHOPPING_BENEFITS") && a.getSubcategory().name().equals("POINT_EXTINCTION_1")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.SHOPPING_BENEFITS, AlarmUserSubcategoryEnum.POINT_EXTINCTION_1,
                    "※ 이 메시지는 이용약관(계약서) 동의에 따라 지급된\n" +
                            "포인트 대한 소멸 예정 안내 메세지입니다.\n" +
                            "\n" +
                            "※ 문자 발송 시점에 따라 포인트에 차이가 있을 수 있으며, \n" +
                            "포인트 유효기간 및 이용 안내는 아래 쿠폰 조회에서\n" +
                            " 확인하실 수 있습니다.",
                    "*해당 알림은 고객님이 보유중인 포인트 만료 \n" +
                            "30일전에 발송됩니다.",
                    AlarmUserSubcategoryEnum.POINT_EXTINCTION_1.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("SHOPPING_BENEFITS") && a.getSubcategory().name().equals("POINT_EXTINCTION_2")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForUser(AlarmUserCategoryEnum.SHOPPING_BENEFITS, AlarmUserSubcategoryEnum.POINT_EXTINCTION_2,
                    "※ 이 메시지는 이용약관(계약서) 동의에 따라 지급된\n" +
                            "포인트 대한 소멸 예정 안내 메세지입니다.\n" +
                            "\n" +
                            "※ 문자 발송 시점에 따라 포인트에 차이가 있을 수 있으며, \n" +
                            "포인트 유효기간 및 이용 안내는 아래 쿠폰 조회에서\n" +
                            " 확인하실 수 있습니다.",
                    "*해당 알림은 고객님이 보유중인 포인트 만료 \n" +
                            "1일전에 발송됩니다.",
                    AlarmUserSubcategoryEnum.POINT_EXTINCTION_2.getTemplate()));

        alarmAdminForUserRepository.saveAll(list);
    }

    private void initializeSellerAlarm() {
        List<AlarmAdminForSeller> all = alarmAdminForSellerRepository.findAll();
        List<AlarmAdminForSeller> list = new ArrayList<>();

        Optional<AlarmAdminForSeller> optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("PAYMENT_COMPLETE")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.ORDER_DELIVERY, AlarmSellerSubcategoryEnum.PAYMENT_COMPLETE,
                    "배송 주문 처리를 부탁드립니다.", AlarmSellerSubcategoryEnum.PAYMENT_COMPLETE.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("DELIVERY_START")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.ORDER_DELIVERY, AlarmSellerSubcategoryEnum.DELIVERY_START,
                    "상세 내역은 관리자 페이지에서 확인하실 수 있습니다.", AlarmSellerSubcategoryEnum.DELIVERY_START.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("DELIVERY_COMPLETE")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.ORDER_DELIVERY, AlarmSellerSubcategoryEnum.DELIVERY_COMPLETE,
                    "상세 내역은 관리자 페이지에서 확인하실 수 있습니다.", AlarmSellerSubcategoryEnum.DELIVERY_COMPLETE.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("ORDER_DELIVERY") && a.getSubcategory().name().equals("CANCEL")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.ORDER_DELIVERY, AlarmSellerSubcategoryEnum.CANCEL,
                    "관리자 페이지에 접수하여 주문 처리를 부탁드립니다.", AlarmSellerSubcategoryEnum.CANCEL.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("WISH") && a.getSubcategory().name().equals("WISH")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.WISH, AlarmSellerSubcategoryEnum.WISH,
                    "관리자 페이지에서 확인 가능합니다.", AlarmSellerSubcategoryEnum.WISH.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("INQUIRY_REVIEW") && a.getSubcategory().name().equals("INQUIRY_REVIEW")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.INQUIRY_REVIEW, AlarmSellerSubcategoryEnum.INQUIRY_REVIEW,
                    "관리자 페이지에서 확인하여 회신/답변 부탁드리겠습니다.", AlarmSellerSubcategoryEnum.INQUIRY_REVIEW.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("OPEN") && a.getSubcategory().name().equals("OPEN_APPROVAL")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.OPEN, AlarmSellerSubcategoryEnum.OPEN_APPROVAL,
                    "관리자 페이지에서 확인하실 수 있습니다.", AlarmSellerSubcategoryEnum.OPEN_APPROVAL.getTemplate()));

        optional = all.stream().filter(a -> a.getCategory().name().equals("OPEN") && a.getSubcategory().name().equals("OPEN_REJECTION")).findFirst();
        if (optional.isEmpty())
            list.add(new AlarmAdminForSeller(AlarmSellerCategoryEnum.OPEN, AlarmSellerSubcategoryEnum.OPEN_REJECTION,
                    "저희 서비스를 찾아주셔서 감사합니다.", AlarmSellerSubcategoryEnum.OPEN_REJECTION.getTemplate()));

        alarmAdminForSellerRepository.saveAll(list);
    }
}
