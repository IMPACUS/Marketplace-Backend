package com.impacus.maketplace.service.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.dto.alarm.user.GetUserAlarmDto;
import com.impacus.maketplace.dto.alarm.user.UpdateUserAlarmDto;
import com.impacus.maketplace.entity.alarm.token.AlarmToken;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import com.impacus.maketplace.repository.alarm.bizgo.AlarmTokenRepository;
import com.impacus.maketplace.repository.alarm.user.AlarmUserRepository;
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
public class AlarmUserService {
    private final AlarmUserRepository alarmUserRepository;
    private final AlarmTokenRepository alarmTokenRepository;

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

    public List<GetUserAlarmDto> findAlarm(Long userId) {
        List<AlarmUser> userAlarmList = alarmUserRepository.findAllByUserId(userId);
        return userAlarmList.stream().map(GetUserAlarmDto::new).collect(Collectors.toList());
    }

    public void updateAlarm(UpdateUserAlarmDto u, Long userId) {
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

    public void saveAndUpdateToken(String token, Long userId) {
        Optional<AlarmToken> optional = alarmTokenRepository.findByUserId(userId);
        if (optional.isEmpty()) alarmTokenRepository.save(new AlarmToken(userId, token));
        else optional.get().setToken(token);
    }
}
