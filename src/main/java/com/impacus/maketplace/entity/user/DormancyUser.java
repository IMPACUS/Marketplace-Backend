package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.utils.TimestampConverter;
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
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime updateDormancyAt;

    @Builder
    public DormancyUser(Long userId, String userName, LocalDateTime updateDormancyAt) {
        this.userId = userId;
        this.userName = userName;
        this.updateDormancyAt = updateDormancyAt;
    }
}
