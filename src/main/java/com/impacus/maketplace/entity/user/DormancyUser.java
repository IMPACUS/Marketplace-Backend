package com.impacus.maketplace.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "dormancy_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DormancyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormancy_user_id")
    private Long id;

    private Long userId;

    private String userName;

    @Setter
    private LocalDateTime updateDormancyDateTime;

    @Builder
    public DormancyUser(Long userId, String userName, LocalDateTime updateDormancyDateTime) {
        this.userId = userId;
        this.userName = userName;
        this.updateDormancyDateTime = updateDormancyDateTime;
    }
}
