package com.impacus.maketplace.service.common.sms;

import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.service.alarm.AlarmSendService;
import org.springframework.stereotype.Service;

@Service
public class BizgoSMSService implements SMSService {
    private AlarmSendService alarmSendService;

    private void validateSMS(String phoneNumber, String message) {
        boolean result = false;
        String errorMessage = "";

        if (message == null || phoneNumber == null) {
            errorMessage = "message 혹은 phoneNumber가 null이 될 수 없습니다.";
        }
        if (!phoneNumber.matches(RegExpPatternConstants.SMS_PHONE_NUMBER_PATTERN)) {
            errorMessage = "핸드폰 번호 형식이 맞지 않습니다.";
        }
        if (message.length() > 90) {
            errorMessage = "메세지 내용은 90 글자 이상을 보낼 수 없습니다.";
        }

        if (result) {
            throw new CustomException(CommonErrorType.INVALID_SMS, errorMessage);
        }
    }

    /**
     * SMS 전송 후, 성공여부 리턴
     *
     * @param phoneNumber 수신번호(형식, 01012345678)
     * @param message     메시지내용 (최대 90 글자)
     * @return 성공 여부
     */
    @Override
    public boolean sendSMS(String phoneNumber, String message) {
        // 유효성 검사
        validateSMS(phoneNumber, message);

        // 메세지 전송

        return false;
    }
}
