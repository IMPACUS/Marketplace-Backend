package com.impacus.maketplace.entity.admin;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Table(name = "admin_login_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "crt_date")
    private String crtDate;

    @Column(name = "status")
    private String status;
}
