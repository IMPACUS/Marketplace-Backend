package com.impacus.maketplace.dto.admin;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminFormDTO {
    private String imgSrc;
    private String name;
    private String phoneNumber;
    private String email;
    private String addr;
    private String juminNo;
    private String accountType;
    private String adminIdName;
    private String password;
    private String recentActivityDate;

//    @QueryProjection
//    public AdminFormDTO(String imgSrc, String name, String phoneNumber, String email, String addr, String juminNo, String accountType, String adminIdName, String password, String recentActivityDate) {
//        this.imgSrc = imgSrc;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.addr = addr;
//        this.juminNo = juminNo;
//        this.accountType = accountType;
//        this.adminIdName = adminIdName;
//        this.password = password;
//        this.recentActivityDate = recentActivityDate;
//    }
}
