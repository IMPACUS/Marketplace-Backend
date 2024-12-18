package com.impacus.maketplace.entity.alarm.admin;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmSellerDTO;
import com.impacus.maketplace.dto.alarm.admin.OutputAlarmSellerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

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
    private AlarmSellerCategoryEnum category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmSellerSubcategoryEnum subcategory;

    @Column(columnDefinition = "text")
    @Comment("알림 내용1")
    private String comment1 = "";

    @Column(columnDefinition = "text")
    @Comment("알림 내용 템플릿")
    private String template;

    public AlarmAdminForSeller(AlarmSellerCategoryEnum category, AlarmSellerSubcategoryEnum subcategory, String comment1, String template) {
        this.category = category;
        this.subcategory = subcategory;
        this.comment1 = comment1;
        this.template = template.replace("#{하단 문구}", comment1);
    }

    public AlarmAdminForSeller(AddAlarmSellerDTO addAlarmSellerDto, AlarmSellerSubcategoryEnum subcategory) {
        this.category = addAlarmSellerDto.getCategory();
        this.subcategory = subcategory;
        List<String> commentList = addAlarmSellerDto.getSubcategory().get(subcategory);
        String template = subcategory.getTemplate();

        if (commentList.size() > 0) {
            this.comment1 = commentList.get(0);
            this.template = template.replace("#{하단 문구}", this.comment1);
        } else {
            this.template = template.replace("#{하단 문구}", "");
        }

        // 차후 알림 입력창이 2개 이상일때 사용
//        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(subcategory.name()) && commentList.size() > 1) {
//            this.comment2 = commentList.get(1);
//            this.template = template.replace("#{하단 문구1}", this.comment2);
//        } else {
//            this.template = template.replace("#{하단 문구1}", "");
//        }
    }

    public OutputAlarmSellerDTO toDto() {
        List<String> commentList;

        // 차후 알림 입력창이 2개 이상일때 사용
//        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(this.subcategory.name()))
//            commentList = List.of(this.comment1, this.comment2);
//        else
        commentList = List.of(this.comment1);
        return new OutputAlarmSellerDTO(this.category, this.subcategory, commentList);
    }
}
