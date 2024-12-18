package com.impacus.maketplace.service.alarm.admin;


import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmUserDTO;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmAdminUserService {
    private final AlarmAdminForUserRepository alarmAdminForUserRepository;

    public void inputValidation(AlarmUserCategoryEnum category, Set<AlarmUserSubcategoryEnum> subcategorySet) {
        String categoryName = category.name();
        for (AlarmUserSubcategoryEnum subcategory : subcategorySet) {
            if (!categoryName.equals(subcategory.getCategory()))
                throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);
        }
    }

    public void add(AddAlarmUserDTO addAlarmDto, AlarmUserSubcategoryEnum subcategoryEnum) {
        alarmAdminForUserRepository.save(addAlarmDto.toEntity(subcategoryEnum));
    }

    @Transactional(readOnly = true)
    public Optional<AlarmAdminForUser> find(AlarmUserCategoryEnum category, AlarmUserSubcategoryEnum subcategory) {
        return alarmAdminForUserRepository.findByCategoryAndSubcategory(category, subcategory);
    }

    public void update(AlarmAdminForUser a, List<String> commentList) {
        String comment1 = "";
        String comment2 = "";
        String template = a.getSubcategory().getTemplate();
        if (commentList.size() > 0) {
            comment1 = commentList.get(0);
            template = template.replace("#{하단 문구}", comment1);
        } else {
            template = template.replace("#{하단 문구}", "");
        }

        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(a.getSubcategory().name()) && commentList.size() > 1) {
            comment2 = commentList.get(1);
            template = template.replace("#{하단 문구1}", comment2);
        } else {
            template = template.replace("#{하단 문구1}", "");
        }
        alarmAdminForUserRepository.updateComment(a.getId(), comment1, comment2, template);
    }
}
