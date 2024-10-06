package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_user_review")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmReview extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_review_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewEnum category;

}
