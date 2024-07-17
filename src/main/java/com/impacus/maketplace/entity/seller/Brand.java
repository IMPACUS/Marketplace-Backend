package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

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

  @Builder
  public Brand(
          Long sellerId,
          String introduction,
          LocalTime openingTime,
          LocalTime closingTime,
          String businessDay,
          String breakingTime
  ) {
    this.sellerId = sellerId;
    this.introduction = introduction;
    this.openingTime = openingTime;
    this.closingTime = closingTime;
    this.businessDay = businessDay;
    this.breakingTime = breakingTime;
  }
}
