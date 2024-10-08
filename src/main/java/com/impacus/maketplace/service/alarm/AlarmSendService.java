package com.impacus.maketplace.service.alarm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.enumType.error.BizgoErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.alarm.common.SendTextDto;
import com.impacus.maketplace.dto.alarm.seller.SendSellerTextDto;
import com.impacus.maketplace.dto.alarm.user.SendUserTextDto;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.entity.alarm.bizgo.BizgoToken;
import com.impacus.maketplace.entity.alarm.seller.AlarmSeller;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForSellerRepository;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForUserRepository;
import com.impacus.maketplace.repository.alarm.bizgo.BizgoRepository;
import com.impacus.maketplace.repository.alarm.seller.AlarmSellerRepository;
import com.impacus.maketplace.repository.alarm.user.AlarmUserRepository;
import com.impacus.maketplace.service.EmailService;
import com.impacus.maketplace.dto.alarm.bizgo.BizgoTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmSendService {
    private final EmailService emailService;
    private final BizgoRepository bizgoRepository;
    private final AlarmUserRepository alarmUserRepository;
    private final AlarmSellerRepository alarmSellerRepository;
    private final AlarmAdminForUserRepository alarmAdminForUserRepository;
    private final AlarmAdminForSellerRepository alarmAdminForSellerRepository;

    @Value("${key.bizgo.id}")
    private String bizgoId;
    @Value("${key.bizgo.pw}")
    private String bizgoPw;
    @Value("${key.bizgo.sender-key}")
    private String senderKey;

    @Transactional(readOnly = true)
    public void sendUserAlarm(Long userId, String receiver, SendUserTextDto sendUserTextDto) {
        AlarmUserCategoryEnum category = sendUserTextDto.getCategory();
        AlarmUserSubcategoryEnum subcategory = sendUserTextDto.getSubcategory();
        Optional<AlarmAdminForUser> optionalAdmin = alarmAdminForUserRepository.findByCategoryAndSubcategory(category, subcategory);
        if (optionalAdmin.isEmpty())
            throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);

        Optional<AlarmUser> optionalUser = alarmUserRepository.findByUserIdAndCategory(userId, category);
        if (optionalUser.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NO_EXIST_USER);

        AlarmUser alarmUser = optionalUser.get();
        if (alarmUser.getIsOn()) {
            String template = optionalAdmin.get().getTemplate();
//        if(alarmUser.getEmail()) this.sendMail();
            if (alarmUser.getKakao())
                this.sendKakao(receiver, sendUserTextDto, template, subcategory.getKakaoCode());
            if (alarmUser.getMsg())
                this.sendUserMsg(receiver, sendUserTextDto, template);
//                    if(alarmUser.getPush())
        }
    }

//    private void sendMail(String receiver, AlarmEnum alarmEnum) {
//        EmailDto emailDto = EmailDto.builder()
//                .receiveEmail(receiver)
//                .build();
//        emailService.sendMail(emailDto, MailType.selectAlarm(alarmEnum));
//    }

    private void sendUserMsg(String phone, SendTextDto sendTextDto, String template) {
        String msg = this.getText(sendTextDto, template);
        String token = this.getToken();
        this.generateMsg(token, msg, phone);
    }

    private String getText(SendTextDto sendTextDto, String template) {
        if (sendTextDto instanceof SendUserTextDto) {
            SendUserTextDto sendUserTextDto = (SendUserTextDto) sendTextDto;
            String subcategory = sendUserTextDto.getSubcategory().name();
            String name = sendUserTextDto.getName();
            String orderDate = sendUserTextDto.getOrderDate();
            String orderNum = sendUserTextDto.getOrderNum();
            String itemName = sendUserTextDto.getItemName();
            String amount = sendUserTextDto.getAmount();
            String courier = sendUserTextDto.getCourier();
            String invoice = sendUserTextDto.getInvoice();
            String couponName = sendUserTextDto.getCouponName();
            String couponAmount = sendUserTextDto.getCouponAmount();
            String couponExpired = sendUserTextDto.getCouponExpired();
            String couponLink = sendUserTextDto.getCouponLink();
            String pointAmount = sendUserTextDto.getPointAmount();
            String pointExpired = sendUserTextDto.getPointExpired();
            String pointLink = sendUserTextDto.getPointLink();

            if (StringUtils.hasText(name)) template = template.replace("#{홍길동}", name);
            else throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_COMMENT_IN_SUBCATEGORY);

            switch (subcategory) {
                case "PAYMENT_COMPLETE":
                    if (StringUtils.hasText(orderDate) && StringUtils.hasText(orderNum) && StringUtils.hasText(itemName) && StringUtils.hasText(amount))
                        return template.replace("#{주문일}", orderDate).replace("#{주문번호}", orderNum).replace("#{상품명}", itemName).replace("#{주문금액}", amount);
                    break;
                case "DELIVERY_START":
                case "DELIVERY_COMPLETE":
                case "CANCEL":
                    if (StringUtils.hasText(itemName) && StringUtils.hasText(orderNum) && StringUtils.hasText(courier) && StringUtils.hasText(invoice))
                        return template.replace("#{상품명}", itemName).replace("#{주문번호}", orderNum).replace("#{택배사}", courier).replace("#{송장번호}", invoice);
                    break;
                case "RESTOCK":
                case "REVIEW":
                    if (StringUtils.hasText(itemName))
                        return template.replace("#{상품명}", itemName);
                    break;
                case "SERVICE_EVALUATION":
                case "SERVICE_CENTER":
                    return template;
                case "COUPON_EXTINCTION_1":
                case "COUPON_EXTINCTION_2":
                    if (StringUtils.hasText(couponName) && StringUtils.hasText(couponAmount) && StringUtils.hasText(couponExpired) && StringUtils.hasText(couponLink))
                        return template.replace("#{쿠폰명}", couponName).replace("#{쿠폰금액}", couponAmount).replace("#{유효기간}", couponExpired).replace("#{쿠폰함 링크}", couponLink);
                    break;
                case "POINT_EXTINCTION_1":
                case "POINT_EXTINCTION_2":
                    if (StringUtils.hasText(pointAmount) && StringUtils.hasText(pointExpired) && StringUtils.hasText(pointLink))
                        return template.replace("#{포인트}", pointAmount).replace("#{유효기간}", pointExpired).replace("#{적립금 링크}", pointLink);
                    break;
            }
        } else if (sendTextDto instanceof SendSellerTextDto) {
            SendSellerTextDto sendSellerTextDto = (SendSellerTextDto) sendTextDto;
            String subcategory = sendSellerTextDto.getSubcategory().name();
            String brand = sendSellerTextDto.getBrand();
            String orderDate = sendSellerTextDto.getOrderDate();
            String orderNum = sendSellerTextDto.getOrderNum();
            String itemName = sendSellerTextDto.getItemName();
            String amount = sendSellerTextDto.getAmount();
            String invoice = sendSellerTextDto.getInvoice();

            if (StringUtils.hasText(brand)) template = template.replace("#{브랜드명}", brand);
            else throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_COMMENT_IN_SUBCATEGORY);

            switch (subcategory) {
                case "PAYMENT_COMPLETE":
                    if (StringUtils.hasText(orderDate) && StringUtils.hasText(orderNum) && StringUtils.hasText(itemName) && StringUtils.hasText(amount))
                        return template.replace("#{주문일}", orderDate).replace("#{주문번호}", orderNum).replace("#{상품명}", itemName).replace("#{주문금액}", amount);
                    break;
                case "DELIVERY_START":
                case "DELIVERY_COMPLETE":
                case "CANCEL":
                    if (StringUtils.hasText(itemName) && StringUtils.hasText(orderNum) && StringUtils.hasText(invoice))
                        return template.replace("#{상품명}", itemName).replace("#{주문번호}", orderNum).replace("#{운송장번호}", invoice);
                    break;
                case "WISH":
                case "INQUIRY_REVIEW":
                case "OPEN_APPROVAL":
                case "OPEN_REJECTION":
                    return template;
            }
        }
        throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_COMMENT_IN_SUBCATEGORY);
    }

    private String getToken() {
        Optional<BizgoToken> optional = bizgoRepository.latestToken();
        String token = "";
        if (optional.isEmpty()) {
            token = this.generateToken();
        } else {
            BizgoToken bizgoToken = optional.get();
            token = bizgoToken.getToken();
            LocalDateTime expiredDate = bizgoToken.getExpiredDate().minusMinutes(1);
            boolean isExpired = expiredDate.isBefore(LocalDateTime.now());
            if (isExpired) token = this.generateToken();
        }
        return token;
    }

    private String generateToken() {
        String url = "https://omni.ibapi.kr/v1/auth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        headers.set("X-IB-Client-Id", bizgoId);       // 발급받은 계정 아이디 입력
        headers.set("X-IB-Client-Passwd", bizgoPw); // 발급받은 계정 비밀번호 입력

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<BizgoTokenDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, BizgoTokenDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                BizgoTokenDto body = response.getBody();
                bizgoRepository.save(body.toEntity());
                return body.getData().getToken();
            } else if (response.getStatusCode().is4xxClientError())
                throw new CustomException(HttpStatus.UNAUTHORIZED, BizgoErrorType.UNAUTHORIZED);
            else
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, BizgoErrorType.UNKNOWN_ERROR);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.COMMUNICATION_ERROR);
        }
    }

    private void generateMsg(String token, String msg, String phone) {
        String url = "https://omni.ibapi.kr/v1/send/sms";
        int bytes = 0;
        try {
            bytes = msg.getBytes("EUC-KR").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (bytes >= 2000) throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.MSG_LIMIT_EXCEED);
        else if (bytes >= 87) url = "https://omni.ibapi.kr/v1/send/mms";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        JSONObject requestBody = new JSONObject();
        requestBody.put("from", "01071644471");
        requestBody.put("to", phone);
        requestBody.put("text", msg);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful())
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.MSG_ERROR);
        } catch (Exception e) {
            log.error("문자 전송 에러 : {}", e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, BizgoErrorType.MSG_ERROR);
        }
    }

    private void sendKakao(String phone, SendTextDto sendTextDto, String template, String kakaoCode) {
        String text = this.getText(sendTextDto, template);
        String token = this.getToken();
        this.generateKakao(token, phone, kakaoCode, text);
    }

    private void generateKakao(String token, String phone, String code, String template) {
        String url = "https://omni.ibapi.kr/v1/send/alimtalk";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        JSONObject requestBody = new JSONObject();
        requestBody.put("senderKey", senderKey);
        requestBody.put("msgType", "AT");
        requestBody.put("to", phone);
        requestBody.put("templateCode", code);
        requestBody.put("text", template);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful())
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_ERROR);
        } catch (Exception e) {
            log.error("카톡 전송 에러 : {}", e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, BizgoErrorType.KAKAO_ERROR);
        }
    }

    public String sendPush(String token, String title, String content) {
        Message message = Message.builder()
                .putData("title", title)
                .putData("content", content)
                .setToken(token)
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.FCM_ERROR);
        }

        log.info("Successfully sent message : {}", response);
        return response;
    }

    public void sendSellerAlarm(Long sellerId, String receiver, SendSellerTextDto sendSellerTextDto) {
        AlarmSellerCategoryEnum category = sendSellerTextDto.getCategory();
        AlarmSellerSubcategoryEnum subcategory = sendSellerTextDto.getSubcategory();
        Optional<AlarmAdminForSeller> optionalAdmin = alarmAdminForSellerRepository.findByCategoryAndSubcategory(category, subcategory);
        if (optionalAdmin.isEmpty())
            throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);

        Optional<AlarmSeller> optionalSeller = alarmSellerRepository.findBySellerIdAndCategory(sellerId, category);
        if (optionalSeller.isEmpty())
            throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NO_EXIST_SELLER);

        AlarmSeller alarmSeller = optionalSeller.get();
        String template = optionalAdmin.get().getTemplate();
//        if(alarmUser.getEmail()) this.sendMail();
        if (alarmSeller.getKakao())
            this.sendKakao(receiver, sendSellerTextDto, template, subcategory.getKakaoCode());
        if (alarmSeller.getMsg()) this.sendUserMsg(receiver, sendSellerTextDto, template);
//                    if(alarmUser.getPush())

    }
}
