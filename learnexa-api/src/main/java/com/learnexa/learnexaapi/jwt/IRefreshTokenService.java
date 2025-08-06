package com.learnexa.learnexaapi.jwt;

import com.learnexa.learnexaapi.entity.RefreshToken;
import com.learnexa.learnexaapi.entity.User;

public interface IRefreshTokenService {
    public RefreshToken createRefreshToken(User user);
    public RefreshToken saveRefreshToken(RefreshToken refreshToken);
    public RefreshToken findByRefreshToken(String refreshToken);
    public boolean isExpireRefreshToken(RefreshToken refreshToken);
}
