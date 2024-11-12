package com.impacus.maketplace.service.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerTimeEnum;
import com.impacus.maketplace.dto.alarm.seller.GetSellerAlarmDto;
import com.impacus.maketplace.dto.alarm.seller.UpdateSellerAlarmDto;
import com.impacus.maketplace.entity.alarm.seller.AlarmSeller;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.alarm.seller.AlarmSellerRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmSellerService {
    private final AlarmSellerRepository alarmSellerRepository;
    private final SellerRepository sellerRepository;

    public void saveDefault(Long userId) {
        List<AlarmSeller> list = new ArrayList<>();
        for (AlarmSellerCategoryEnum category : AlarmSellerCategoryEnum.values()) {
            boolean kakao = true;
            boolean email = true;
            boolean msg = false;
            list.add(new AlarmSeller(userId, category, kakao, email, msg, AlarmSellerTimeEnum.All));
        }
        alarmSellerRepository.saveAll(list);
    }

    public void updateAlarm(UpdateSellerAlarmDto u, Long userId) {
        AlarmSellerCategoryEnum category = u.getCategory();
        Boolean kakao = u.getKakao();
        Boolean email = u.getEmail();
        Boolean msg = u.getMsg();
        AlarmSellerTimeEnum time = u.getTime();

        Optional<AlarmSeller> optional = alarmSellerRepository.findByUserIdAndCategory(userId, category);
        if (optional.isPresent()) {
            AlarmSeller alarmSeller = optional.get();
            alarmSeller.update(kakao, email, msg, time);
        } else {
            alarmSellerRepository.save(new AlarmSeller(userId, category, kakao, email, msg, time));
        }
    }

    public List<GetSellerAlarmDto> findAlarm(Long userId) {
        List<AlarmSeller> alarmSellerList = alarmSellerRepository.findAllByUserId(userId);
        return alarmSellerList.stream().map(GetSellerAlarmDto::new).collect(Collectors.toList());
    }
}
