package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IAuthController;
import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.entity.pojo.RootEntity;
import com.learnexa.learnexaapi.dto.auth.AuthRequest;
import com.learnexa.learnexaapi.dto.auth.AuthResponse;
import com.learnexa.learnexaapi.dto.auth.RefreshTokenRequest;
import com.learnexa.learnexaapi.dto.auth.RegisterRequest;
import com.learnexa.learnexaapi.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/auth")
public class AuthControllerImpl implements IAuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    @Override
    public ResponseEntity<RootEntity<DtoUser>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        DtoUser response = authService.register(registerRequest);
        return RootEntity.ok(response, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.authenticate(authRequest);
        return RootEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Override
    public RootEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse response = authService.refreshToken(refreshTokenRequest);
        return RootEntity.ok(response);
    }
}
