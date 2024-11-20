package com.impacus.maketplace.service.auth;

import NiceID.Check.CPClient;
import com.impacus.maketplace.common.enumType.certification.CPClientErrorCode;
import com.impacus.maketplace.common.enumType.certification.CerticationEncryptionErrorCode;
import com.impacus.maketplace.common.enumType.certification.CertificationResultCode;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.auth.response.CertificationRequestDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NiceAPIService {
    private static final String CERTIFICATION_RESULT_URI = "/api/v1/auth/certification";
    private static final String RESULT_KEY = "result";
    @Value("${key.nice.site-code}")
    private String sSiteCode;
    @Value("${key.nice.password}")
    private String sSitePassword;
    @Value("${url.server-host}")
    private String serverHost;

    /**
     * 암호화 데이터를 읽을 CPCClient를 생성하는 함수
     *
     * @param encodeData
     * @return
     */
    public CPClient getCPClient(String encodeData) {
        CPClient client = new CPClient();
        int resultCode = client.fnDecode(sSiteCode, sSitePassword, encodeData);

        // 에러 확인
        if (resultCode != 0) {
            CPClientErrorCode errorCode = CPClientErrorCode.fromCode(resultCode);
            throw new CustomException(UserErrorType.FAIL_TO_CERTIFICATION, errorCode);
        }

        return client;
    }

    /**
     * 요청 데이터 생성
     *
     * @param isTest test용인 경우는 웹페이지, 실사용도인 경우에는 모바일 설정을 사용
     * @return
     */
    public CertificationRequestDataDTO getRequestData(boolean isTest) {
        CPClient client = new CPClient();

        // 요청 번호 생성
        String requestNumber = client.getRequestNO(sSiteCode);

        // 회원사 및 설정 정보 설정
        Map<String, String> plainDataMap = Map.of(
                "REQ_SEQ", requestNumber,
                "SITECODE", sSiteCode,
                "AUTH_TYPE", "", // "": 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
                "RTN_URL", buildReturnUrl(CertificationResultCode.SUCCESS),
                "ERR_URL", buildReturnUrl(CertificationResultCode.FAIL),
                "POPUP_GUBUN", "N", // Y : 취소버튼 있음,  N : 취소버튼 없음
                "CUSTOMIZE", isTest ? "" : "Mobile", // "": 기본 웹페이지, Mobile : 모바일페이지
                "GENDER", "" // "": 기본 선택 값, 0 : 여자, 1 : 남자
        );

        // Plain 데이터 생성
        String plainData = buildPlainData(plainDataMap);

        // 암호화
        int iReturn = client.fnEncode(sSiteCode, sSitePassword, plainData);
        if (iReturn != 0) {
            CerticationEncryptionErrorCode errorCode = CerticationEncryptionErrorCode.fromCode(iReturn);
            throw new CustomException(UserErrorType.FAIL_TO_ENCRYPT_CERTIFICATION, errorCode.getMessage());
        }

        // DTO 생성
        return CertificationRequestDataDTO.toDTO(
                client.getCipherData(),
                requestNumber
        );
    }

    private String buildReturnUrl(CertificationResultCode resultCode) {
        return serverHost + CERTIFICATION_RESULT_URI + "?" + RESULT_KEY + "=" + resultCode;
    }

    private String buildPlainData(Map<String, String> dataMap) {
        StringBuilder plainDataBuilder = new StringBuilder();
        dataMap.forEach((key, value) ->
                plainDataBuilder.append(key.length()).append(":").append(key)
                        .append(value.getBytes().length).append(":").append(value)
        );
        return plainDataBuilder.toString();
    }
}
