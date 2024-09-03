package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.dto.alarm.user.add.AddReviewDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateBrandShopDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateReviewDto;
import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_review")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmReview extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_review_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewEnum content;

    public AlarmReview(AddReviewDto s, Long userId) {
        this.userId = userId;
        this.content = s.getContent();
        this.comment1 = s.getComment1();
        this.comment2 = s.getComment2();
        this.email = s.getEmail();
        this.kakao = s.getKakao();
        this.msg = s.getMsg();
        this.push = s.getPush();
    }

    public void updateAlarm(UpdateReviewDto u) {
        this.comment1 = u.getComment1();
        this.comment2 = u.getComment2();
        this.email = u.getEmail();
        this.kakao = u.getKakao();
        this.msg = u.getMsg();
        this.push = u.getPush();
    }
}
