package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.dto.UserInfoDto;
import com.learnexa.learnexaapi.entity.pojo.RootEntity;
import org.springframework.http.ResponseEntity;

public interface IUserInfoController {
    public ResponseEntity<RootEntity<UserInfoDto>> userInfo(Long id);
}
