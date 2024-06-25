package com.impacus.maketplace.entity.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Entity
@Getter
@Table(name = "admin_info")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "img_src")
    private String imgSrc;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "addr")
    private String addr;

    @Column(name = "jumin_no")
    private String juminNo;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "admin_id_name")
    private String adminIdName;

    @Column(name = "password")
    private String password;

    @Column(name = "recent_activity_date")
    private ZonedDateTime recentActivityDate;

}
