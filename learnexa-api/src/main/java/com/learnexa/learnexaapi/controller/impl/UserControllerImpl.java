package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IUserController;
import com.learnexa.learnexaapi.entity.RootEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserControllerImpl implements IUserController {

    @GetMapping("/test-user")
    @Override
    public RootEntity<String> helloUser() {
        return RootEntity.ok("Hello User :D");
    }
}
