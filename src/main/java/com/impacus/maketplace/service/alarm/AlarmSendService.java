package com.impacus.maketplace.service.alarm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.impacus.maketplace.common.enumType.alarm.*;
import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.enumType.error.BizgoErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.dto.alarm.common.SendTextDto;
import com.impacus.maketplace.dto.alarm.seller.SendSellerTextDto;
import com.impacus.maketplace.dto.alarm.user.SendUserTextDto;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.entity.alarm.token.AlarmToken;
import com.impacus.maketplace.entity.alarm.seller.AlarmHold;
import com.impacus.maketplace.entity.alarm.seller.AlarmSeller;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import com.impacus.maketplace.entity.seller.Brand;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForSellerRepository;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForUserRepository;
import com.impacus.maketplace.repository.alarm.bizgo.AlarmTokenRepository;
import com.impacus.maketplace.repository.alarm.seller.AlarmHoldRepository;
import com.impacus.maketplace.repository.alarm.seller.AlarmSellerRepository;
import com.impacus.maketplace.repository.alarm.user.AlarmUserRepository;
import com.impacus.maketplace.repository.seller.BrandRepository;
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
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmSendService {
    private final EmailService emailService;
    private final AlarmTokenRepository alarmTokenRepository;
    private final AlarmUserRepository alarmUserRepository;
    private final AlarmSellerRepository alarmSellerRepository;
    private final AlarmAdminForUserRepository alarmAdminForUserRepository;
    private final AlarmAdminForSellerRepository alarmAdminForSellerRepository;
    private final AlarmHoldRepository alarmHoldRepository;
    private final BrandRepository brandRepository;

    @Value("${key.bizgo.id}")
    private String bizgoId;
    @Value("${key.bizgo.pw}")
    private String bizgoPw;
    @Value("${key.bizgo.sender-key}")
    private String senderKey;
    @Value("${key.bizgo.from-phone}")
    private String fromPhone;

    public void sendUserAlarm(Long userId, String receiver, String phone, SendUserTextDto sendUserTextDto) {
        AlarmUserCategoryEnum category = sendUserTextDto.getCategory();
        AlarmUserSubcategoryEnum subcategory = sendUserTextDto.getSubcategory();
        Optional<AlarmAdminForUser> optionalAdmin = alarmAdminForUserRepository.findByCategoryAndSubcategory(category, subcategory);
        if (optionalAdmin.isEmpty())
            throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);

        Optional<AlarmUser> optionalUser = alarmUserRepository.findByUserIdAndCategory(userId, category);
        if (optionalUser.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NO_EXIST_USER);

        AlarmUser alarmUser = optionalUser.get();
        Boolean isEmail = alarmUser.getEmail();
        Boolean isKakao = alarmUser.getKakao();
        Boolean isMsg = alarmUser.getMsg();
        String kakaoCode = subcategory.getKakaoCode();
        String subject = subcategory.getValue();
        if (alarmUser.getIsOn()) {
            String template = optionalAdmin.get().getTemplate();
            String text = this.getText(sendUserTextDto, template);
            this.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text);
        }
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

    void sendAlarm(Boolean isKakao, Boolean isEmail, Boolean isMsg, String subject, String receiver, String phone, String kakaoCode, String text) {
        if (isEmail)
            this.sendMail(receiver, subject, text);
        if (isKakao || isMsg) {
            String token = this.getToken();
            if (isKakao)
                this.sendKakao(token, phone, text, kakaoCode);
            if (isMsg)
                this.sendMsg(token, phone, text);
        }
    }

    private String getToken() {
        Optional<AlarmToken> optional = alarmTokenRepository.findBizgoToken();
        String token = "";
        if (optional.isEmpty()) {
            token = this.generateToken(null);
        } else {
            AlarmToken alarmToken = optional.get();
            token = alarmToken.getToken();
            LocalDateTime expiredDate = alarmToken.getExpiredDate().minusMinutes(1);
            boolean isExpired = expiredDate.isBefore(LocalDateTime.now());
            if (isExpired) token = this.generateToken(alarmToken.getId());
        }
        return token;
    }

    private String generateToken(Long id) {
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
                if (id == null) alarmTokenRepository.save(body.toEntity());
                else {
                    String expiredData = body.getData().getExpired().replace("+09:00", "");
                    alarmTokenRepository.updateToken(body.getData().getToken(), LocalDateTime.parse(expiredData), LocalDateTime.now(), id);
                }
                return body.getData().getToken();
            } else if (response.getStatusCode().is4xxClientError())
                throw new CustomException(HttpStatus.UNAUTHORIZED, BizgoErrorType.UNAUTHORIZED);
            else
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, BizgoErrorType.UNKNOWN_ERROR);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.COMMUNICATION_ERROR);
        }
    }

    private void sendMail(String receiver, String subject, String text) {
        String subjectMail = "[IMPLACE] " + subject + " 안내입니다.";
        EmailDto emailDto = EmailDto.builder()
                .receiveEmail(receiver)
                .subject(subjectMail)
                .build();
        String mailText = "<div style=\"margin:100px;\">" +
                "  <h1>안녕하세요.</h1>" +
                "  <h1>환경을 먼저 생각하는 IMPLACE 입니다.</h1>" +
                "  <br>" +
                "  <p>#{안내메일}</p>" +
                "  <br/>" +
                "  <br/>" +
                "</div>";
        mailText = mailText.replace("#{안내메일}", text).replace("\n", "<br/>");
        emailService.sendAlarmMail(emailDto, mailText);
    }

    private void sendMsg(String token, String phone, String msg) {
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
        requestBody.put("from", fromPhone);
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

    private void sendKakao(String token, String phone, String text, String kakaoCode) {
        String url = "https://omni.ibapi.kr/v1/send/alimtalk";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        JSONObject requestBody = new JSONObject();
        requestBody.put("senderKey", senderKey);
        requestBody.put("msgType", "AT");
        requestBody.put("to", phone);
        requestBody.put("templateCode", kakaoCode);
        requestBody.put("text", text);

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

    public void sendSellerAlarm(Long sellerId, String receiver, String phone, SendSellerTextDto sendSellerTextDto) {
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
        String text = this.getText(sendSellerTextDto, template);
        Boolean isEmail = alarmSeller.getEmail();
        Boolean isKakao = alarmSeller.getKakao();
        Boolean isMsg = alarmSeller.getMsg();
        String subject = subcategory.getValue();
        String kakaoCode = subcategory.getKakaoCode();
        AlarmSellerTimeEnum time = alarmSeller.getTime();

        if (time.equals(AlarmSellerTimeEnum.All)) {
            this.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text);
        } else if (time.equals(AlarmSellerTimeEnum.FIX)) {
            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.of(9, 0);  // 9:00 AM
            LocalTime end = LocalTime.of(22, 0);   // 10:00 PM

            if (now.isAfter(start) && now.isBefore(end))
                this.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text);
            else
                alarmHoldRepository.save(new AlarmHold(start, isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text));
        } else if (time.equals(AlarmSellerTimeEnum.CUSTOM)) {
            Optional<Brand> optional = brandRepository.findBySellerId(sellerId);
            if (optional.isEmpty())
                throw new CustomException(HttpStatus.MULTI_STATUS, AlarmErrorType.NO_EXIST_SELLER_IN_BRAND);

            Brand brand = optional.get();
            LocalTime openingTime = brand.getOpeningTime();
            LocalTime closingTime = brand.getClosingTime();
            LocalTime now = LocalTime.now();

            if (now.isAfter(openingTime) && now.isBefore(closingTime))
                this.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text);
            else
                alarmHoldRepository.save(new AlarmHold(openingTime, isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text));
        }
    }
}
