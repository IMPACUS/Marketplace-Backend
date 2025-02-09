package com.impacus.maketplace.dto.qna.request;

import com.impacus.maketplace.entity.qna.QuestionReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class QuestionReplyDTO {
    @NotBlank
    @Size(max = 500)
    private String contents;

    public QuestionReply toEntity(Long questionId) {
        return new QuestionReply(questionId, this.contents);
    }
}
