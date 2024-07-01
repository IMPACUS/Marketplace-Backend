package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "green_label_point")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GreenLabelPoint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "green_label_point_id")
    private Long id;

    @Comment("user_info의 FK")
    @Column(nullable = false, unique = true)
    private Long userId;

    @ColumnDefault("0")
    @Column(nullable = false)
    @Comment("그린 라벨 포인트")
    private Long greenLabelPoint;
}
