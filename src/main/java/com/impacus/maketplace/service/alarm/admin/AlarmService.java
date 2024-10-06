package com.impacus.maketplace.service.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmSellerDto;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmUserDto;
import com.impacus.maketplace.dto.alarm.admin.GetAlarmUserDto;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForSellerRepository;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
    private final AlarmAdminForUserRepository alarmAdminForUserRepository;
    private final AlarmAdminForSellerRepository alarmAdminForSellerRepository;

    public void inputValidation(AlarmCategoryUserEnum category, Set<AlarmSubcategoryUserEnum> subcategorySet) {
        String categoryName = category.name();
        for (AlarmSubcategoryUserEnum subcategory : subcategorySet) {
            if (!categoryName.equals(subcategory.getCategory()))
                throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);
        }
    }

    public void add(AddAlarmUserDto addAlarmDto, AlarmSubcategoryUserEnum subcategoryEnum) {
        alarmAdminForUserRepository.save(addAlarmDto.toEntity(subcategoryEnum));
    }

    @Transactional(readOnly = true)
    public Optional<AlarmAdminForUser> find(AlarmCategoryUserEnum category, AlarmSubcategoryUserEnum subcategory) {
        return alarmAdminForUserRepository.findByCategoryAndSubcategory(category, subcategory);
    }

    public void update(AlarmAdminForUser a, List<String> commentList) {
        String comment1 = "";
        String comment2 = "";
        String template = a.getSubcategory().getTemplate();
        if (commentList.size() > 0) {
            comment1 = commentList.get(0);
            template = template.replace("#{유저 결제완료}", comment1);
        } else {
            template = template.replace("#{유저 결제완료}", "");
        }

        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(a.getSubcategory().name()) && commentList.size() > 1) {
            comment2 = commentList.get(1);
            template = template.replace("#{유저 결제완료1}", comment2);
        } else {
            template = template.replace("#{유저 결제완료1}", "");
        }
        alarmAdminForUserRepository.updateComment(a.getId(), comment1, comment2, template);
    }

//
//    public void inputValidation(AlarmCategorySellerEnum category, AlarmSubcategorySellerEnum subcategory) {
//        String category1 = category.name();
//        String category2 = subcategory.getCategory();
//        if (!category1.equals(category2))
//            throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);
//    }
//
//    public void add(AddAlarmSellerDto addAlarmDto) {
//        alarmAdminForSellerRepository.save(addAlarmDto.toEntity());
//    }
//
//    public Optional<AlarmAdminForSeller> find(AddAlarmSellerDto addAlarmDto) {
//        return alarmAdminForSellerRepository.findByCategoryAndSubcategory(addAlarmDto.getCategory(), addAlarmDto.getSubcategory());
//    }

}
