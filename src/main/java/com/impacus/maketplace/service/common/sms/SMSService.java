package com.impacus.maketplace.service.common.sms;

/**
 * SMS 전송 및 관리 가능한 서비스 인터페이스
 */
public interface SMSService {

    /**
     * 국내 문자 SMS를 옵션 설정 없이 전송 후, 성공여부 리턴
     *
     * @param phoneNumber 수신번호(형식, 01012345678)
     * @param message     메시지내용 (최대 90 글자)
     * @return 성공 여부
     */
    boolean sendSimpleSMS(String phoneNumber, String message);
}
