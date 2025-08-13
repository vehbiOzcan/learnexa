package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.dto.UserDto;
import com.learnexa.learnexaapi.dto.auth.*;
import com.learnexa.learnexaapi.entity.pojo.RootEntity;
import org.springframework.http.ResponseEntity;

public interface IAuthController {
    public ResponseEntity<RootEntity<UserDto>> register(RegisterRequest registerRequest);
    public ResponseEntity<RootEntity<LoginResponse>> authenticate(AuthRequest authRequest);
    public ResponseEntity<RootEntity<AuthResponse>> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
