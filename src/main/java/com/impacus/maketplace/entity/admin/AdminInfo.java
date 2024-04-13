package com.impacus.maketplace.entity.admin;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Builder
@Table(name = "admin_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
