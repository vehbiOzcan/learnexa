package com.learnexa.learnexaapi.service.impl;

import com.learnexa.learnexaapi.dto.UserInfoDto;
import com.learnexa.learnexaapi.entity.UserInfo;
import com.learnexa.learnexaapi.entity.UserInfoBadge;
import com.learnexa.learnexaapi.exception.BaseException;
import com.learnexa.learnexaapi.exception.ErrorMessage;
import com.learnexa.learnexaapi.exception.MessageType;
import com.learnexa.learnexaapi.repository.*;
import com.learnexa.learnexaapi.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PomodoroRepository pomodoroRepository;
    @Autowired
    private GoalsRepository goalsRepository;
    @Autowired
    private UserInfoBadgeRepository userInfoBadgeRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public UserInfoDto getUserInfo(Long userId) {

         if(userInfoRepository.findUserInfoByUserId(userId).isPresent()){
             UserInfo userInfo =  userInfoRepository.findUserInfoByUserId(userId).get();

             return UserInfoDto.builder()
                     .id(userInfo.getId())
                     .score(userInfo.getScore())
                     .rank(userInfo.getRank())
                     .star(userInfo.getStar())
                     .series(userInfo.getSeries())
                     .pomodoros(pomodoroRepository.findByUserInfo(userInfo))
                     .goals(goalsRepository.findByUserInfo((userInfo)))
                     .badges(
                             userInfoBadgeRepository.findUserInfoBadgeByUserInfo(userInfo)
                                     .stream()
                                     .map(UserInfoBadge::getBadge)
                                     .filter(Objects::nonNull)
                                     .toList()
                     )
                     .lessons(lessonRepository.findByUserId(userId))
                     .build();
         }else {
             throw new BaseException(new ErrorMessage(MessageType.USER_INFOS_NOT_FOUND));
         }

    }
}
