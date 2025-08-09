package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.dto.auth.*;
import com.learnexa.learnexaapi.entity.pojo.RootEntity;
import org.springframework.http.ResponseEntity;

public interface IAuthController {
    public ResponseEntity<RootEntity<DtoUser>> register(RegisterRequest registerRequest);
    public RootEntity<LoginResponse> authenticate(AuthRequest authRequest);
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
