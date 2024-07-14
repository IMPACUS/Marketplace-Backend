package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "brand")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "brand_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private Long sellerId;

  @Column(nullable = false, columnDefinition = "TEXT")
  @Comment("쇼핑몰 소개")
  private String introduction;

  @Column(nullable = false)
  @Comment("영업 시작 시간")
  private LocalTime openingTime;

  @Column(nullable = false)
  @Comment("영업 마무리 시간")
  private LocalTime closingTime;

  @Column(nullable = false)
  @Comment("영업 일자")
  private String businessDay;

  @Column(nullable = false)
  @Comment("점심 시간")
  private String breakingTime;
}
