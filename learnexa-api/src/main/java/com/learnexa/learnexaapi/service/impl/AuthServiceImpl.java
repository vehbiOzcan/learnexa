package com.learnexa.learnexaapi.service.impl;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.entity.AccessToken;
import com.learnexa.learnexaapi.entity.RefreshToken;
import com.learnexa.learnexaapi.entity.Role;
import com.learnexa.learnexaapi.entity.User;
import com.learnexa.learnexaapi.exception.BaseException;
import com.learnexa.learnexaapi.exception.ErrorMessage;
import com.learnexa.learnexaapi.exception.MessageType;
import com.learnexa.learnexaapi.jwt.IJwtService;
import com.learnexa.learnexaapi.jwt.IRefreshTokenService;
import com.learnexa.learnexaapi.jwt.dto.AuthRequest;
import com.learnexa.learnexaapi.jwt.dto.AuthResponse;
import com.learnexa.learnexaapi.jwt.dto.RefreshTokenRequest;
import com.learnexa.learnexaapi.repository.UserRepository;
import com.learnexa.learnexaapi.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private IRefreshTokenService refreshTokenService;

    @Override
    public DtoUser register(AuthRequest authRequest) {
        DtoUser dtoUser = new DtoUser();
        User user = new User();
        if(userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            throw new BaseException(new ErrorMessage(MessageType.USER_ALREADY_EXISTS));
        }
        user.setUsername(authRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        BeanUtils.copyProperties(savedUser, dtoUser);
        dtoUser.setRole(savedUser.getRole());

        return dtoUser;
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {

       try {
           UsernamePasswordAuthenticationToken authentication =
                   new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

           /// Bu kısım zaten kullanıcı yoksa loadByUsername kısmında ovverride ettiğimiz yerden hatayı fırlatır
           authenticationProvider.authenticate(authentication);

           Optional<User> optionalUser = userRepository.findByUsername(authRequest.getUsername());
           User user = optionalUser.get();

           jwtService.allTokenPassiveFromUserId(user.getId());

           /// AccessToken Dbye kayıt ediliyor
           AccessToken accessTokenObj = new AccessToken();
           accessTokenObj.setAccessToken(jwtService.generateToken(user));
           accessTokenObj.setUser(user);
           /// Kayıt edilen AccessTokenden token değeri alınıyor
           String accessToken = jwtService.saveAccessToken(accessTokenObj).getAccessToken();

           RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
           RefreshToken dbRefreshToken = refreshTokenService.saveRefreshToken(refreshToken);

           return new AuthResponse(accessToken,dbRefreshToken.getRefreshToken());

       } catch (InternalAuthenticationServiceException e) {
           throw new BaseException(new ErrorMessage(MessageType.VALIDATION_ERROR,": [ " + e.getMessage() + " ]"));
       } catch (BaseException e) {
           throw new BaseException(new ErrorMessage(e.getMessageType()));
       }catch (BadCredentialsException e){
           throw new BaseException(new ErrorMessage(MessageType.PASSWORD_WRONG));
       }
       catch (Exception e) {
           System.out.println(e.getClass());
           throw new BaseException(new ErrorMessage(MessageType.INTERNAL_SERVER_ERROR));
       }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        if(refreshTokenRequest.getRefreshToken() == null || refreshTokenRequest.getRefreshToken().isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NULL));
        }

        RefreshToken refreshToken = refreshTokenService.findByRefreshToken(refreshTokenRequest.getRefreshToken());

        if (refreshToken == null) {
            //System.out.println("REFRESH TOKEN IS NULL" + refreshTokenRequest.getRefreshToken());
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND));
        }

        if (refreshTokenService.isExpireRefreshToken(refreshToken)) {
            //System.out.println("REFRESH TOKEN IS EXPIRED" + refreshToken.getRefreshToken());
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_EXPIRED));
        }

        /// Gönderilen AccessToken gerçekten var mı kontrolü sağlanıyor.
        AccessToken oldAccessToken = jwtService.findByAccessToken(refreshTokenRequest.getAccessToken());

        if (oldAccessToken == null) {
            throw new BaseException(new ErrorMessage(MessageType.MISSING_JWT));
        }

        if (!oldAccessToken.isActive()) {
            throw new BaseException(new ErrorMessage(MessageType.DEACTIVE_JWT));
        }
        /// Bulunan token pasife çekiliyor
//      oldAccessToken.setActive(false);
        System.out.println("USER ID : " + oldAccessToken.getUser().getId().getClass());
        jwtService.allTokenPassiveFromUserId(oldAccessToken.getUser().getId());
        AccessToken oldDbAccessToken = jwtService.saveAccessToken(oldAccessToken);

        if(oldDbAccessToken == null) {
            throw new BaseException(new ErrorMessage(MessageType.JWT_NOT_REFRESH));
        }

        AccessToken accessToken = new AccessToken();
        String token = jwtService.generateToken(refreshToken.getUser());

        accessToken.setAccessToken(token);
        accessToken.setUser(refreshToken.getUser());

        jwtService.saveAccessToken(accessToken);
        RefreshToken dbRefreshToken = refreshTokenService.saveRefreshToken(
                refreshTokenService.createRefreshToken(refreshToken.getUser())
        );
        System.out.println("BURALARA GELDi 3");
        return new AuthResponse(token,dbRefreshToken.getRefreshToken());
    }

}
