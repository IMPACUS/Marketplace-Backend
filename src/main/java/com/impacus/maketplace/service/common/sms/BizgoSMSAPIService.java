package com.impacus.maketplace.service.common.sms;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.constants.api.BizgoAPIConstants;
import com.impacus.maketplace.dto.common.request.BizgoSMSRequest;
import com.impacus.maketplace.dto.common.response.BizgoSMSResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "bizgoSMSAPICLient",
        url = BizgoAPIConstants.COMMON_URL
)
public interface BizgoSMSAPIService {

    @GetMapping(value = BizgoAPIConstants.SEND_SIMPLE_SMS,
            headers = "Content-Type=application/json;Accept=application/json")
    BizgoSMSResponse sendSimpleSMS(
            @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization,
            @RequestBody BizgoSMSRequest request
    );
}
