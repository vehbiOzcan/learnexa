package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.entity.RootEntity;
import com.learnexa.learnexaapi.jwt.dto.AuthRequest;
import com.learnexa.learnexaapi.jwt.dto.AuthResponse;
import com.learnexa.learnexaapi.jwt.dto.RefreshTokenRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthController {
    public ResponseEntity<RootEntity<DtoUser>> register(AuthRequest authRequest);
    public RootEntity<AuthResponse> authenticate(AuthRequest authRequest);
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
