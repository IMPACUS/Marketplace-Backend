package com.impacus.maketplace.service.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.common.enumType.error.AlarmErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.alarm.admin.AddAlarmSellerDto;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForSellerRepository;
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
public class AlarmSellerService {
    private final AlarmAdminForSellerRepository alarmAdminForSellerRepository;

    public void inputValidation(AlarmCategorySellerEnum category, Set<AlarmSubcategorySellerEnum> subcategorySet) {
        String categoryName = category.name();
        for (AlarmSubcategorySellerEnum subcategory : subcategorySet) {
            if (!categoryName.equals(subcategory.getCategory()))
                throw new CustomException(HttpStatus.BAD_REQUEST, AlarmErrorType.NOT_MATCH_CATEGORY);
        }
    }

    public void add(AddAlarmSellerDto addAlarmDto, AlarmSubcategorySellerEnum subcategoryEnum) {
        alarmAdminForSellerRepository.save(addAlarmDto.toEntity(subcategoryEnum));
    }

    @Transactional(readOnly = true)
    public Optional<AlarmAdminForSeller> find(AlarmCategorySellerEnum category, AlarmSubcategorySellerEnum subcategory) {
        return alarmAdminForSellerRepository.findByCategoryAndSubcategory(category, subcategory);
    }

    public void update(AlarmAdminForSeller a, List<String> commentList) {
        String comment1 = "";
        String comment2 = "";
        String template = a.getSubcategory().getTemplate();
        if (commentList.size() > 0) {
            comment1 = commentList.get(0);
            template = template.replace("#{하단 문구}", comment1);
        } else {
            template = template.replace("#{하단 문구}", "");
        }

        // 차후 입력창이 2개 이상일때 사용
//        if (List.of("COUPON_EXTINCTION_1", "COUPON_EXTINCTION_2", "POINT_EXTINCTION_1", "POINT_EXTINCTION_2").contains(a.getSubcategory().name()) && commentList.size() > 1) {
//            comment2 = commentList.get(1);
//            template = template.replace("#{하단 문구1}", comment2);
//        } else {
//            template = template.replace("#{하단 문구1}", "");
//        }
        alarmAdminForSellerRepository.updateComment(a.getId(), comment1, comment2, template);
    }
}
