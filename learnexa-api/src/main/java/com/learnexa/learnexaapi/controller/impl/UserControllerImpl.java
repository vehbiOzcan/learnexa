package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IUserInfoController;
import com.learnexa.learnexaapi.dto.UserInfoDto;
import com.learnexa.learnexaapi.entity.pojo.RootEntity;
import com.learnexa.learnexaapi.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserControllerImpl implements IUserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("info/{id}")
    @Override
    public ResponseEntity<RootEntity<UserInfoDto>> userInfo(@PathVariable("id") Long id) {

        return RootEntity.ok(userInfoService.getUserInfo(id));
    }

    @GetMapping("info")
    public String userInfo() {

        return "Hello World!";
    }

}
