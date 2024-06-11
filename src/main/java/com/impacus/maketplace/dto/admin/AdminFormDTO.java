package com.impacus.maketplace.dto.admin;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminFormDTO {
    private Long userId; // 인덱스 번호
    private String id; // 아이디
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private String addr;
    private String userJumin1;
    private String accountType;
    private Long profileImageId;

    @QueryProjection
    public AdminFormDTO(Long userId, String name, String phoneNumber, String email, String addr, Long profileImageId) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.addr = addr;
        this.profileImageId = profileImageId;
    }
}
