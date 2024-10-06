package com.impacus.maketplace.entity.alarm.user;

import com.impacus.maketplace.entity.alarm.user.enums.ShoppingBenefitsEnum;
import com.impacus.maketplace.entity.alarm.user.enums.ShoppingBenefitsSubEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alarm_user_shopping_benefits")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmShoppingBenefits extends Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_shopping_benefits_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShoppingBenefitsEnum category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShoppingBenefitsSubEnum subcategory;

}
