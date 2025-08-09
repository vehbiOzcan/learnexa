package com.learnexa.learnexaapi.service.impl;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.dto.auth.*;
import com.learnexa.learnexaapi.entity.AccessToken;
import com.learnexa.learnexaapi.entity.RefreshToken;
import com.learnexa.learnexaapi.entity.UserInfo;
import com.learnexa.learnexaapi.entity.enums.Role;
import com.learnexa.learnexaapi.entity.User;
import com.learnexa.learnexaapi.exception.BaseException;
import com.learnexa.learnexaapi.exception.ErrorMessage;
import com.learnexa.learnexaapi.exception.MessageType;
import com.learnexa.learnexaapi.jwt.IJwtService;
import com.learnexa.learnexaapi.jwt.IRefreshTokenService;
import com.learnexa.learnexaapi.repository.UserInfoRepository;
import com.learnexa.learnexaapi.repository.UserRepository;
import com.learnexa.learnexaapi.service.IAuthService;
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

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public DtoUser register(RegisterRequest registerRequest) {
        DtoUser dtoUser = new DtoUser();
        User user = new User();
        if(userRepository.findByUsername(registerRequest.getEmail().split("@")[0]).isPresent()) {
            throw new BaseException(new ErrorMessage(MessageType.USER_ALREADY_EXISTS));
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setStar(0);
        userInfo.setScore(0);
        userInfo.setRank(0);

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setUsername(registerRequest.getEmail().split("@")[0]);
        user.setEmail(registerRequest.getEmail());
        user.setFullname(registerRequest.getFullname());
        user.setUserInfo(savedUserInfo);


        User savedUser = userRepository.save(user);
        BeanUtils.copyProperties(savedUser, dtoUser);
        dtoUser.setRole(savedUser.getRole());
        dtoUser.setFullname(savedUser.getFullname());

        return dtoUser;
    }

    @Override
    public LoginResponse authenticate(AuthRequest authRequest) {

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

           DtoUser dtoUser = DtoUser.builder()
                   .id(user.getId())
                   .username(user.getUsername())
                   .fullname(user.getFullname())
                   .role(user.getRole())
                   .build();

           RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
           RefreshToken dbRefreshToken = refreshTokenService.saveRefreshToken(refreshToken);

           return new LoginResponse(accessToken,dbRefreshToken.getRefreshToken(),dtoUser);

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
