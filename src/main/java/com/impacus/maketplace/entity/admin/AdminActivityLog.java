package com.impacus.maketplace.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Entity
@Getter
@Table(name = "admin_activity_log")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "crtDate")
    private ZonedDateTime crtDate;

    @Column(name = "activity_detail")
    private String activityDetail;
}
