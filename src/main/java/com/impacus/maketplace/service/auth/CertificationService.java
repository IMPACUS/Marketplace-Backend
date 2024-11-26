package com.impacus.maketplace.service.auth;

import NiceID.Check.CPClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.impacus.maketplace.common.enumType.certification.CertificationResultCode;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.auth.CertificationResult;
import com.impacus.maketplace.dto.auth.response.CertificationRequestDataDTO;
import com.impacus.maketplace.redis.service.CertificationRequestNumberService;
import com.impacus.maketplace.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Enumeration;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificationService {
    private static final String REQ_NUM_KEY = "REQ_SEQ";
    private static final String USER_ID_KEY = "USER_ID";

    private final NiceAPIService niceAPIService;
    private final CertificationRequestNumberService certReqNumberService;
    private final UserService userService;

    @Value("${url.server-host}")
    private String host;

    @Value("${url.uri.certification}")
    private String certificationURI;

    /**
     * 사용자 인증 정보를 저장하는 함수
     *
     * @param result
     * @param encodeData
     */
    public HttpHeaders saveUserCertification(
            CertificationResultCode result,
            Long userId,
            String encodeData,
            HttpSession session
    ) {
        try {
            CPClient client = niceAPIService.getCPClient(encodeData);

            // 1. 암호화 데이터 분석
            String plainData = client.getPlainData();

            // 2. 세션값 확인
            // - 존재하지 않는 경우: 에러 발생
            // - 존재하는 경우: 삭제
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);
                log.info("Session Attribute: " + attributeName + " = " + attributeValue);
            }

            // TODO 세션 초기화 문제
//            Long userId = Long.valueOf((Integer) session.getAttribute(USER_ID_KEY));
//            String sessionReqNumber = (String) session.getAttribute(REQ_NUM_KEY);
//            if (!certReqNumberService.existsCertificationRequestNumber(sessionReqNumber)) {
//                throw new CustomException(UserErrorType.FAIL_TO_CERTIFICATION, CPClientErrorCode.NOT_MATCH_SESSION_NUMBER);
//            } else {
//                certReqNumberService.deleteCertificationRequestNumber(sessionReqNumber);
//            }

            // 3. 데이터 추출
            HashMap mapresult = client.fnParse(plainData);
            CertificationResult certificationResult = new ObjectMapper().convertValue(mapresult, CertificationResult.class);
            writeCertificationLog(userId, certificationResult);

            // 4. 사용자 보안인증 정보 저장
            userService.saveCertification(userId, certificationResult);

            // 5. 성공 정보 전달
            return createRedirectHeaders(getCertificationRedirectURL(), CertificationResultCode.SUCCESS, null, null);
        } catch (Exception e) {
            log.error("Fail to save certification", e);
            return handleCertificationException(e);
        }
    }

    private void writeCertificationLog(Long userId, CertificationResult certificationResult) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("gender", certificationResult.getGender());
        jsonObject.addProperty("nationalInfo", certificationResult.getNationalInfo());
        jsonObject.addProperty("mobileCo", certificationResult.getMobileCo());
        jsonObject.addProperty("name", certificationResult.getName());
        jsonObject.addProperty("authType", certificationResult.getAuthType());

        LogUtils.writeInfoLog("saveUserCertification", jsonObject.toString());
    }

    public CertificationRequestDataDTO getCertificationRequestData(Long userId) {
        return getCertificationRequestData(userId, false);
    }

    /**
     * 본인인증 암호화 데이터 조회
     *
     * @return
     */
    public CertificationRequestDataDTO getCertificationRequestData(Long userId, boolean isTest) {
        // CertificationRequestDataDTO 생성
        CertificationRequestDataDTO dto = niceAPIService.getRequestData(userId, isTest);

        // CertificationRequestNumber 저장
        certReqNumberService.saveCertificationRequestNumber(dto.getReqNumber());

        return dto;
    }

    private HttpHeaders createRedirectHeaders(
            String baseUrl,
            CertificationResultCode result,
            String code,
            String detail
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("result", result);

        if (code != null) builder.queryParam("code", code);
        if (detail != null) builder.queryParam("detail", detail);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(builder.build().toUri());
        return httpHeaders;
    }

    private HttpHeaders handleCertificationException(Exception e) {
        String code;
        String detail;

        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            code = customException.getErrorType().getCode();
            detail = customException.getDetail().toString();
        } else {
            code = CommonErrorType.UNKNOWN.getCode();
            detail = e.getMessage();
        }

        return createRedirectHeaders(getCertificationRedirectURL(), CertificationResultCode.FAIL, code, detail);
    }

    private String getCertificationRedirectURL() {
        return host + certificationURI;
    }

}
