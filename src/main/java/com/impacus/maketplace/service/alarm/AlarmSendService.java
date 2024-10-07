package com.impacus.maketplace.service.alarm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.error.BizgoErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.entity.alarm.bizgo.BizgoToken;
import com.impacus.maketplace.repository.alarm.bizgo.BizgoRepository;
import com.impacus.maketplace.service.EmailService;
import com.impacus.maketplace.dto.alarm.bizgo.BizgoTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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

    @Value("${key.bizgo.id}")
    private String bizgoId;
    @Value("${key.bizgo.pw}")
    private String bizgoPw;
    @Value("${key.bizgo.sender-key}")
    private String senderKey;

//    public void sendMail(String receiver, AlarmEnum alarmEnum) {
//        EmailDto emailDto = EmailDto.builder()
//                .receiveEmail(receiver)
//                .build();
//        emailService.sendMail(emailDto, MailType.selectAlarm(alarmEnum));
//    }

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

//    public void sendKakao(String phone, AlarmKakaoEnum kakaoEnum, String name, String itemName, String orderNum, String courier, String invoice) {
//        if (!StringUtils.hasText(name)) throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_NAME);
//        String template = kakaoEnum.getTemplate().replace("#{홍길동}", name);
//        String kakao = kakaoEnum.name();
//
//        if (kakao.equals("CANCEL")) {
//            if (!StringUtils.hasText(itemName))
//                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_ITEM_NAME);
//            if (!StringUtils.hasText(orderNum))
//                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_ORDER_NUM);
//            if (!StringUtils.hasText(courier))
//                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_COURIER);
//            if (!StringUtils.hasText(invoice))
//                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_INVOICE);
//            template = template.replace("#{상품명}", itemName).replace("#{주문번호}", orderNum).replace("#{택배사}", courier).replace("#{송장번호}", invoice);
//        } else if (kakao.equals("RESTOCK") || kakao.equals("REVIEW")) {
//            if (!StringUtils.hasText(itemName))
//                throw new CustomException(HttpStatus.BAD_REQUEST, BizgoErrorType.KAKAO_NO_ITEM_NAME);
//            template = template.replace("#{상품명}", itemName);
//        }
//        String token = this.getToken();
//        this.generateKakao(token, phone, kakaoEnum.getCode(), template);
//    }

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
}
