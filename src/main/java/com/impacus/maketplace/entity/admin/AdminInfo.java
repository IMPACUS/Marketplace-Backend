package com.impacus.maketplace.entity.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "activity_detail")
    private String activityDetail;

}
