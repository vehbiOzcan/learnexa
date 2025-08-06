package com.learnexa.learnexaapi.jwt.impl;

import com.learnexa.learnexaapi.entity.AccessToken;
import com.learnexa.learnexaapi.entity.RefreshToken;
import com.learnexa.learnexaapi.jwt.IJwtService;
import com.learnexa.learnexaapi.repository.AccessTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements IJwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private Long expirationTime;

    @Autowired
    private AccessTokenRepository  accessTokenRepository;

    @Override
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey())
                .compact();
    }

    @Override
    public AccessToken saveAccessToken(AccessToken accessToken) {
        return accessTokenRepository.save(accessToken);
    }


    public AccessToken findByAccessToken(String accessToken) {

        if (accessToken == null) return null;

        Optional<AccessToken> optionalAccessToken = accessTokenRepository.findByAccessToken(accessToken);

        if (optionalAccessToken.isPresent()) return optionalAccessToken.get();

        return null;
    }

    @Override
    public void allTokenPassiveFromUserId(Long userId) {
        accessTokenRepository.updateAccessToken(userId);
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    @Override
    public <T> T exportToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String getUsernameByToken(String token) {
        return exportToken(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Date expireDate = exportToken(token, Claims::getExpiration);
        return new Date().after(expireDate);
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        String usernameFromToken = exportToken(token, Claims::getSubject);
        return (username.equals(usernameFromToken) && !isTokenExpired(token));
    }

    @Override
    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
