package com.impacus.maketplace.repository.alarm.user;

import com.impacus.maketplace.entity.alarm.user.AlarmOrderDelivery;
import com.impacus.maketplace.entity.alarm.user.AlarmReview;
import com.impacus.maketplace.repository.alarm.user.querydsl.AlarmReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmReviewRepository extends JpaRepository<AlarmReview, Long>, AlarmReviewRepositoryCustom {
}
