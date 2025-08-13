package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.UserInfoDto;

public interface IUserInfoService {
    public UserInfoDto getUserInfo(Long userId);
}
