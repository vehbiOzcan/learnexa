package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IAdminController;
import com.learnexa.learnexaapi.entity.RootEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class AdminControllerImpl implements IAdminController {

    @GetMapping("/test-admin")
    @Override
    public RootEntity<String> helloAdmin() {
        return RootEntity.ok("Hello Admin :D");
    }
}
