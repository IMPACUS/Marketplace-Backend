package com.impacus.maketplace.entity.sharedLink;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.SharedLinkType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "shared_link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedLink extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shared_link_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "shared_user_id", nullable = false)
    @Comment("공유 받은 유저 아이디")
    private Long sharedUserId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SharedLinkType type;
}
