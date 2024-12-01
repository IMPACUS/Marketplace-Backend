package com.impacus.maketplace.service.common.sms;

import com.impacus.maketplace.common.constants.BaseConstants;
import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import com.impacus.maketplace.common.constants.api.BizgoAPIConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.common.request.BizgoSMSRequest;
import com.impacus.maketplace.dto.common.response.BizgoSMSResponse;
import com.impacus.maketplace.service.alarm.AlarmSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class BizgoSMSService implements SMSService {
    private AlarmSendService alarmSendService;
    private BizgoSMSAPIService bizgoSMSAPIService;

    @Value("${key.bizgo.from-phone}")
    private String callingNumber;

    private void validateSMS(String phoneNumber, String message) throws UnsupportedEncodingException {
        boolean result = false;
        String errorMessage = "";

        if (message == null || phoneNumber == null) {
            errorMessage = "message 혹은 phoneNumber가 null이 될 수 없습니다.";
        }
        if (!phoneNumber.matches(RegExpPatternConstants.SMS_PHONE_NUMBER_PATTERN)) {
            errorMessage = "핸드폰 번호 형식이 맞지 않습니다.";
        }
        if (message.getBytes(BaseConstants.CHARSET_EUC_KR).length > 90) {
            errorMessage = "메세지 내용은 90 글자 이상을 보낼 수 없습니다.";
        }

        if (result) {
            throw new CustomException(CommonErrorType.INVALID_SMS, errorMessage);
        }
    }

    /**
     * 국내 문자 SMS 를 옵션 설정 없이 전송 후, 성공여부 리턴
     *
     * @param phoneNumber 수신번호(형식, 01012345678)
     * @param message     메시지내용 (최대 90 글자)
     * @return 성공 여부
     */
    @Override
    public boolean sendSimpleSMS(String phoneNumber, String message) {
        try {
            // 유효성 검사
            validateSMS(phoneNumber, message);

            String token = alarmSendService.getToken();
            // 메세지 전송
            BizgoSMSRequest request = BizgoSMSRequest.toRequest(
                    callingNumber,
                    phoneNumber,
                    message
            );
            BizgoSMSResponse response = bizgoSMSAPIService.sendSimpleSMS(
                    token,
                    request
            );
            if (response.getCode().equals(BizgoAPIConstants.API_SUCCESS_CODE)) {
                return true;
            } else {
                LogUtils.writeErrorLog("sendSimpleSMS", response.toString());
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }
}
