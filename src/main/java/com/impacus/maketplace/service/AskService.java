package com.impacus.maketplace.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.AskDto;
import com.impacus.maketplace.entity.common.Ask;
import com.impacus.maketplace.repository.AskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AskService {

    private final AskRepository askRepository;
    private final ObjectCopyHelper objectCopyHelper;

    @Transactional
    public boolean sendAsk(AskDto askDto) {
        Ask askEntity = objectCopyHelper.copyObject(askDto, Ask.class);
        Ask result = askRepository.save(askEntity);

        //TODO: 추후 Discord나 Slack 에 Hook 연결?
        return result != null ? true : false;
    }

    public List<Ask> loadAskListForAdmin(AskDto askDto) {
        return askRepository.findAll();
    }

    public List<AskDto> loadAskListForClient(AskDto askDto) {
        List<Ask> askEntityList = askRepository.findAllByRegisterId(askDto.getRegisterId());
        List<AskDto> result = askEntityList.stream()
                .map(ask -> objectCopyHelper.copyObject(ask, AskDto.class))
                .collect(Collectors.toList());
        return result;

    }

}
