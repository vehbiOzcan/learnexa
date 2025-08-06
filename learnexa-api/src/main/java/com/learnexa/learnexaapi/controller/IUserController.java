package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.entity.RootEntity;
import org.springframework.http.ResponseEntity;

public interface IUserController {
    public RootEntity<String> helloUser();
}
