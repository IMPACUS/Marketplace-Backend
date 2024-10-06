package com.impacus.maketplace.entity.alarm.admin;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmSellerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "alarm_admin_for_seller")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmAdminForSeller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmCategoryUserEnum category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmSubcategoryUserEnum subcategory;

    @Column(columnDefinition = "text")
    @Comment("알림 내용1")
    private String comment1;

    @Column(columnDefinition = "text")
    @Comment("알림 내용2")
    private String comment2;

    @Column(columnDefinition = "text")
    @Comment("알림 내용 템플릿")
    private String template;

    public AlarmAdminForSeller(AddAlarmSellerDto addAlarmSellerDto){


    }
}
