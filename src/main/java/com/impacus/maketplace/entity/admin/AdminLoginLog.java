package com.impacus.maketplace.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Entity
@Getter
@Table(name = "admin_login_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AdminLoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "crt_date")
    private ZonedDateTime crtDate;

    @Column(name = "status")
    private String status;
}
