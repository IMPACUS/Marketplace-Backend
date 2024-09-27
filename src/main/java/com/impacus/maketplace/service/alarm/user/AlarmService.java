package com.impacus.maketplace.service.alarm.user;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.enumType.error.BizgoErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.dto.alarm.user.add.*;
import com.impacus.maketplace.dto.alarm.user.update.*;
import com.impacus.maketplace.entity.alarm.bizgo.BizgoToken;
import com.impacus.maketplace.entity.alarm.user.*;
import com.impacus.maketplace.entity.alarm.user.enums.*;
import com.impacus.maketplace.repository.alarm.bizgo.BizgoRepository;
import com.impacus.maketplace.repository.alarm.user.*;
import com.impacus.maketplace.service.EmailService;
import com.impacus.maketplace.service.alarm.user.dto.BizgoTokenDto;
import com.impacus.maketplace.service.alarm.user.enums.AlarmEnum;
import com.impacus.maketplace.service.alarm.user.enums.AlarmKakaoEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmService {
    private final AlarmBrandShopRepository alarmBrandShopRepository;
    private final AlarmShoppingBenefitsRepository alarmShoppingBenefitsRepository;
    private final AlarmOrderDeliveryRepository alarmOrderDeliveryRepository;
    private final AlarmRestockRepository alarmRestockRepository;
    private final AlarmReviewRepository alarmReviewRepository;
    private final AlarmServiceCenterRepository alarmServiceCenterRepository;
    private final EmailService emailService;
    private final BizgoRepository bizgoRepository;

    @Value("${key.bizgo.id}")
    private String bizgoId;
    @Value("${key.bizgo.pw}")
    private String bizgoPw;
    @Value("${key.bizgo.sender-key}")
    private String senderKey;

    public void add(Object saveDto, Long userId) {
        if (saveDto instanceof AddOrderDeliveryDto) {
            AddOrderDeliveryDto orderDeliveryDto = (AddOrderDeliveryDto) saveDto;
            alarmOrderDeliveryRepository.save(orderDeliveryDto.toEntity(userId));
        } else if (saveDto instanceof AddBrandShopDto) {
            AddBrandShopDto brandShopDto = (AddBrandShopDto) saveDto;
            alarmBrandShopRepository.save(brandShopDto.toEntity(userId));
        } else if (saveDto instanceof AddReviewDto) {
            AddReviewDto reviewDto = (AddReviewDto) saveDto;
            alarmReviewRepository.save(reviewDto.toEntity(userId));
        } else if (saveDto instanceof AddRestockDto) {
            AddRestockDto restockDto = (AddRestockDto) saveDto;
            alarmRestockRepository.save(restockDto.toEntity(userId));
        } else if (saveDto instanceof AddShoppingBenefitsDto) {
            AddShoppingBenefitsDto shoppingBenefitsDto = (AddShoppingBenefitsDto) saveDto;
            alarmShoppingBenefitsRepository.save(shoppingBenefitsDto.toEntity(userId));
        } else if (saveDto instanceof AddServiceCenterDto) {
            AddServiceCenterDto serviceCenterDto = (AddServiceCenterDto) saveDto;
            alarmServiceCenterRepository.save(serviceCenterDto.toEntity(userId));
        } else {
            throw new IllegalArgumentException("존재하지 않는 DTO입니다.");
        }
    }

    @Transactional(readOnly = true)
    public Object find(Object content, Long userId) {
        if (content instanceof OrderDeliveryEnum) {
            OrderDeliveryEnum orderDelivery = (OrderDeliveryEnum) content;
            return alarmOrderDeliveryRepository.findData(orderDelivery, userId);
        } else if (content instanceof BrandShopEnum) {
            BrandShopEnum brandShop = (BrandShopEnum) content;
            return alarmBrandShopRepository.findData(brandShop, userId);
        } else if (content instanceof ReviewEnum) {
            ReviewEnum review = (ReviewEnum) content;
            return alarmReviewRepository.findData(review, userId);
        } else if (content instanceof RestockEnum) {
            RestockEnum restock = (RestockEnum) content;
            return alarmRestockRepository.findData(restock, userId);
        } else if (content instanceof ShoppingBenefitsEnum) {
            ShoppingBenefitsEnum shoppingBenefits = (ShoppingBenefitsEnum) content;
            return alarmShoppingBenefitsRepository.findData(shoppingBenefits, userId);
        } else if (content instanceof ServiceCenterEnum) {
            ServiceCenterEnum serviceCenter = (ServiceCenterEnum) content;
            return alarmServiceCenterRepository.findData(serviceCenter, userId);
        } else {
            throw new IllegalArgumentException("존재하지 않는 Enum입니다.");
        }
    }

    public void update(Long id, Object updateDto, Long userId) {
        if (updateDto instanceof UpdateBrandShopDto) {
            UpdateBrandShopDto updateBrandShopDto = (UpdateBrandShopDto) updateDto;
            Optional<AlarmBrandShop> byId = alarmBrandShopRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmBrandShop alarmBrandShop = byId.get();
            if (alarmBrandShop.getUserId() != userId) throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmBrandShop.updateAlarm(updateBrandShopDto);
        } else if (updateDto instanceof UpdateOrderDeliveryDto) {
            UpdateOrderDeliveryDto updateOrderDeliveryDto = (UpdateOrderDeliveryDto) updateDto;
            Optional<AlarmOrderDelivery> byId = alarmOrderDeliveryRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmOrderDelivery alarmOrderDelivery = byId.get();
            if (alarmOrderDelivery.getUserId() != userId)
                throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmOrderDelivery.updateAlarm(updateOrderDeliveryDto);
        } else if (updateDto instanceof UpdateRestockDto) {
            UpdateRestockDto updateRestockDto = (UpdateRestockDto) updateDto;
            Optional<AlarmRestock> byId = alarmRestockRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmRestock alarmRestock = byId.get();
            if (alarmRestock.getUserId() != userId) throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmRestock.updateAlarm(updateRestockDto);
        } else if (updateDto instanceof UpdateReviewDto) {
            UpdateReviewDto updateReviewDto = (UpdateReviewDto) updateDto;
            Optional<AlarmReview> byId = alarmReviewRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmReview alarmReview = byId.get();
            if (alarmReview.getUserId() != userId) throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmReview.updateAlarm(updateReviewDto);
        } else if (updateDto instanceof UpdateShoppingBenefitsDto) {
            UpdateShoppingBenefitsDto updateShoppingBenefitsDto = (UpdateShoppingBenefitsDto) updateDto;
            Optional<AlarmShoppingBenefits> byId = alarmShoppingBenefitsRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmShoppingBenefits alarmShoppingBenefits = byId.get();
            if (alarmShoppingBenefits.getUserId() != userId)
                throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmShoppingBenefits.updateAlarm(updateShoppingBenefitsDto);
        } else if (updateDto instanceof UpdateServiceCenterDto) {
            UpdateServiceCenterDto updateServiceCenterDto = (UpdateServiceCenterDto) updateDto;
            Optional<AlarmServiceCenter> byId = alarmServiceCenterRepository.findById(id);
            if (byId.isEmpty()) throw new CustomException(AlarmErrorType.INVALID_ALARM_ID);

            AlarmServiceCenter alarmServiceCenter = byId.get();
            if (alarmServiceCenter.getUserId() != userId)
                throw new CustomException(AlarmErrorType.INVALID_USER_ID);
            alarmServiceCenter.updateAlarm(updateServiceCenterDto);
        } else {
            throw new IllegalArgumentException("존재하지 않는 Dto입니다.");
        }
    }

    public void sendMail(String receiver, AlarmEnum alarmEnum) {
        EmailDto emailDto = EmailDto.builder()
                .receiveEmail(receiver)
                .build();
        emailService.sendMail(emailDto, MailType.selectAlarm(alarmEnum));
    }

    public void sendMsg(String msg, String phone) {
        String token = this.getToken();
        this.generateMsg(token, msg, phone);
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
        else if (bytes > 87) url = "https://omni.ibapi.kr/v1/send/mms";

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

    public void sendKakao(String phone, AlarmKakaoEnum kakaoEnum, String name, String itemName, String orderNum, String courier, String invoice) {
        if (!StringUtils.hasText(name)) throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_NAME);
        String template = kakaoEnum.getTemplate().replace("#{홍길동}", name);
        String kakao = kakaoEnum.name();

        if (kakao.equals("CANCEL")) {
            if (!StringUtils.hasText(itemName))
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_ITEM_NAME);
            if (!StringUtils.hasText(orderNum))
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_ORDER_NUM);
            if (!StringUtils.hasText(courier))
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_COURIER);
            if (!StringUtils.hasText(invoice))
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_INVOICE);
            template = template.replace("#{상품명}", itemName).replace("#{주문번호}", orderNum).replace("#{택배사}", courier).replace("#{송장번호}", invoice);
        } else if (kakao.equals("RESTOCK") || kakao.equals("REVIEW")) {
            if (!StringUtils.hasText(itemName))
                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_ITEM_NAME);
            template = template.replace("#{상품명}", itemName);
        }
        String token = this.getToken();
        this.generateKakao(token, phone, kakaoEnum.getCode(), template);
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
}
