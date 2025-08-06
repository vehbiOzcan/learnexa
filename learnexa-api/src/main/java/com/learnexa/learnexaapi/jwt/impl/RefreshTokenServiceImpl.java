package com.learnexa.learnexaapi.jwt.impl;

import com.learnexa.learnexaapi.entity.RefreshToken;
import com.learnexa.learnexaapi.entity.User;
import com.learnexa.learnexaapi.jwt.IRefreshTokenService;
import com.learnexa.learnexaapi.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    @Value("${jwt.refresh.expirationTime}")
    private Long refreshExpirationTime;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + refreshExpirationTime));
        refreshToken.setUser(user);

        return refreshToken;
    }

    @Override
    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {

        if (refreshToken == null) return null;

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (optionalRefreshToken.isPresent()) return optionalRefreshToken.get();

        return null;
    }

    @Override
    public boolean isExpireRefreshToken(RefreshToken refreshToken) {
        return new Date().after(refreshToken.getExpireDate());
    }

}
