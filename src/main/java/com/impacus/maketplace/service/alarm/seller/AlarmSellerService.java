package com.impacus.maketplace.service.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum;
import com.impacus.maketplace.dto.alarm.seller.UpdateSellerAlarmDto;
import com.impacus.maketplace.entity.alarm.seller.AlarmSeller;
import com.impacus.maketplace.repository.alarm.seller.AlarmSellerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmSellerService {
    private final AlarmSellerRepository alarmSellerRepository;

    public void saveDefault(Long sellerId) {
        List<AlarmSeller> list = new ArrayList<>();
        for (AlarmSellerCategoryEnum category : AlarmSellerCategoryEnum.values()) {
            boolean kakao = true;
            boolean email = true;
            boolean msg = false;
            list.add(new AlarmSeller(sellerId, category, kakao, email, msg, AlarmSellerTimeEnum.All));
        }
        alarmSellerRepository.saveAll(list);
    }

    public void updateAlarm(@Valid UpdateSellerAlarmDto u, Long sellerId) {
        AlarmSellerCategoryEnum category = u.getCategory();
        Boolean kakao = u.getKakao();
        Boolean email = u.getEmail();
        Boolean msg = u.getMsg();
        AlarmSellerTimeEnum time = u.getTime();

        Optional<AlarmSeller> optional = alarmSellerRepository.findBySellerIdAndCategory(sellerId, category);
        if (optional.isPresent()) {
            AlarmSeller alarmSeller = optional.get();
            alarmSeller.update(kakao, email, msg, time);
        } else {
            alarmSellerRepository.save(new AlarmSeller(sellerId, category, kakao, email, msg, time));
        }
    }
}
