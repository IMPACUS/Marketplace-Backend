package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.AskDto;
import com.impacus.maketplace.entity.common.Ask;
import com.impacus.maketplace.service.AskService;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.configurers.ServletApiConfigurer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ask")
public class AskController {

    private final AskService askService;

    @PostMapping("/send")
    public ApiResponseEntity<?> sendAsk(@RequestBody AskDto askDto) {
        boolean result = askService.sendAsk(askDto);

        return ApiResponseEntity
                .builder()
                .result(result)
                .build();
    }

    @PostMapping("/list")
    public ApiResponseEntity<List<AskDto>> loadAskList(@RequestBody AskDto askDto) {
        ApiResponseEntity<List<AskDto>> res = new ApiResponseEntity<>();

        List<AskDto> askList = askService.loadAskListForClient(askDto);

        if (CollectionUtils.isNotEmpty(askList)) {
            res.setResult(false);
        } else {
            res.setData(askList);
        }

        return res;
    }


}
