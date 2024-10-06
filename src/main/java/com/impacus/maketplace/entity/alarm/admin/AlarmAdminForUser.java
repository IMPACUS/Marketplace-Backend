package com.impacus.maketplace.entity.alarm.admin;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmUserDto;
import com.impacus.maketplace.dto.alarm.admin.OutputAlarmUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "alarm_admin_for_user")
@AllArgsConstructor
@NoArgsConstructor
public class AlarmAdminForUser extends BaseEntity {
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
    private String comment1 = "";

    @Column(columnDefinition = "text")
    @Comment("알림 내용2")
    private String comment2 = "";

    @Column(columnDefinition = "text")
    @Comment("알림 내용 템플릿")
    private String template;

    public AlarmAdminForUser(AddAlarmUserDto addAlarmUserDto, AlarmSubcategoryUserEnum subcategory) {
        this.category = addAlarmUserDto.getCategory();
        this.subcategory = subcategory;
        List<String> commentList = addAlarmUserDto.getSubcategory().get(subcategory);
        String template = subcategory.getTemplate();

        if (commentList.size() > 0) {
            this.comment1 = commentList.get(0);
            this.template = template.replace("#{유저 결제완료}", this.comment1);
        } else {
            this.template = template.replace("#{유저 결제완료}", "");
        }

        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(subcategory.name()) && commentList.size() > 1) {
            this.comment2 = commentList.get(1);
            this.template = template.replace("#{유저 결제완료1}", this.comment2);
        } else {
            this.template = template.replace("#{유저 결제완료1}", "");
        }
    }

    public OutputAlarmUserDto toDto() {
        List<String> commentList;
        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(this.subcategory.name()))
            commentList = List.of(this.comment1, this.comment2);
        else
            commentList = List.of(this.comment1);
        return new OutputAlarmUserDto(this.category, this.subcategory, commentList);
    }
}
