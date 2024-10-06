package com.impacus.maketplace.entity.alarm.seller;


import com.impacus.maketplace.entity.alarm.seller.enums.InquiryEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_seller_inquiry")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmInquiry extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_inquiry_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryEnum category;
}
