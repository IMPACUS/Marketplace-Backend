package com.impacus.maketplace.entity.admin;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Builder
@Table(name = "admin_activity_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "crtDate")
    private String crtDate;

    @Column(name = "activity_detail")
    private String activityDetail;
}
