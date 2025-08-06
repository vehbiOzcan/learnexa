package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.entity.RootEntity;
import org.springframework.http.ResponseEntity;

public interface IAdminController {
    public RootEntity<String> helloAdmin();
}
