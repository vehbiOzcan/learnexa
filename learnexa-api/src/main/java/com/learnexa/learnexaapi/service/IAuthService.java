package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.dto.auth.*;

public interface IAuthService {
    public DtoUser register(RegisterRequest registerRequest);
    public LoginResponse authenticate(AuthRequest authRequest);
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
