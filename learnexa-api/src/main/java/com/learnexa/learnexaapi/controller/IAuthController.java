package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.entity.pojo.RootEntity;
import com.learnexa.learnexaapi.dto.auth.AuthRequest;
import com.learnexa.learnexaapi.dto.auth.AuthResponse;
import com.learnexa.learnexaapi.dto.auth.RefreshTokenRequest;
import com.learnexa.learnexaapi.dto.auth.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthController {
    public ResponseEntity<RootEntity<DtoUser>> register(RegisterRequest registerRequest);
    public RootEntity<AuthResponse> authenticate(AuthRequest authRequest);
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
