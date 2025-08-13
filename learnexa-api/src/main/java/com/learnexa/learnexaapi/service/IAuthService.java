package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.UserDto;
import com.learnexa.learnexaapi.dto.auth.*;

public interface IAuthService {
    public UserDto register(RegisterRequest registerRequest);
    public LoginResponse authenticate(AuthRequest authRequest);
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
