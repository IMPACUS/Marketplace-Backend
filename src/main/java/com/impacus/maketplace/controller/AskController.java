package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.AskDto;
import com.impacus.maketplace.service.AskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
