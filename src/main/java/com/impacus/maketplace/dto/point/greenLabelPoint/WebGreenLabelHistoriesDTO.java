package com.impacus.maketplace.dto.point.greenLabelPoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class WebGreenLabelHistoriesDTO {
    private Page<WebGreenLabelHistoryDTO> histories;
    private long totalCount = 0L;
    private long totalPointPrice = 0L;

    public WebGreenLabelHistoriesDTO(Page<WebGreenLabelHistoryDTO> histories) {
        this.histories = histories;
        this.totalCount = histories.getNumberOfElements();
        for (WebGreenLabelHistoryDTO historyDTO : histories) {
            totalPointPrice += historyDTO.getTradeAmount();
        }
    }

    public static WebGreenLabelHistoriesDTO toDTO(Page<WebGreenLabelHistoryDTO> histories) {
        return new WebGreenLabelHistoriesDTO(histories);
    }
}
