package com.impacus.maketplace.dto.qna.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WebQuestionDTO {
    private Long questionId;

    @ExcelColumn(headerName = "문의 내용")
    private String contents;

    @ExcelColumn(headerName = "주문 번호")
    private String orderId;

    @ExcelColumn(headerName = "주문자")
    private String userName;

    @ExcelColumn(headerName = "아이디")
    private String userEmail;

    @ExcelColumn(headerName = "등록일자")
    private LocalDateTime createdAt;

    private boolean hasReply;

    @JsonIgnore
    @ExcelColumn(headerName = "답변 여부")
    private String strHasReply;
}
