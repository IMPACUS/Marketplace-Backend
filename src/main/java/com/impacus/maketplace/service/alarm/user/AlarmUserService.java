package com.impacus.maketplace.service.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.dto.alarm.user.UpdateUserAlarmDto;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import com.impacus.maketplace.repository.alarm.user.AlarmUserRepository;
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
public class AlarmUserService {
    private final AlarmUserRepository alarmUserRepository;

    public void saveDefault(Long userId) {
        List<AlarmUser> list = new ArrayList<>();
        for (AlarmUserCategoryEnum category : AlarmUserCategoryEnum.values()) {
            boolean kakao = true;
            boolean push = true;
            boolean msg = false;
            boolean email = false;
            if (category.name().equals("SERVICE_CENTER")) {
                push = false;
                email = true;
            }
            list.add(new AlarmUser(userId, category, kakao, push, msg, email));
        }
        alarmUserRepository.saveAll(list);
    }

    public void updateAlarm(@Valid UpdateUserAlarmDto u, Long userId) {
        AlarmUserCategoryEnum category = u.getCategory();
        Boolean isOn = u.getIsOn();
        Boolean kakao = u.getKakao();
        Boolean push = u.getPush();
        Boolean msg = u.getMsg();
        Boolean email = u.getEmail();

        Optional<AlarmUser> optional = alarmUserRepository.findByUserIdAndCategory(userId, category);
        if (optional.isPresent()) {
            AlarmUser alarmUser = optional.get();
            alarmUser.update(isOn, kakao, push, msg, email);
        } else {
            alarmUserRepository.save(new AlarmUser(userId, category, kakao, push, msg, email));
        }
    }
}
