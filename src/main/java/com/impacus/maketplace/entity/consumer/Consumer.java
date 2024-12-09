package com.impacus.maketplace.entity.consumer;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "consumer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consumer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_id")
    private Long id;

    @Comment("user_info의 FK")
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String ci;

    public Consumer(Long userId, String ci) {
        this.userId = userId;
        this.ci = ci;
    }
}
