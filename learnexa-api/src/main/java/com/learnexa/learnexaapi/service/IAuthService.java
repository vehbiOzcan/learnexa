package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.dto.auth.AuthRequest;
import com.learnexa.learnexaapi.dto.auth.AuthResponse;
import com.learnexa.learnexaapi.dto.auth.RefreshTokenRequest;
import com.learnexa.learnexaapi.dto.auth.RegisterRequest;

public interface IAuthService {
    public DtoUser register(RegisterRequest registerRequest);
    public AuthResponse authenticate(AuthRequest authRequest);
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
