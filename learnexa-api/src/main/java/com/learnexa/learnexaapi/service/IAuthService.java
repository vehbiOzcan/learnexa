package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.jwt.dto.AuthRequest;
import com.learnexa.learnexaapi.jwt.dto.AuthResponse;
import com.learnexa.learnexaapi.jwt.dto.RefreshTokenRequest;

public interface IAuthService {
    public DtoUser register(AuthRequest authRequest);
    public AuthResponse authenticate(AuthRequest authRequest);
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
