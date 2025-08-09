package com.learnexa.learnexaapi.dto.auth;

import com.learnexa.learnexaapi.dto.DtoUser;
import com.learnexa.learnexaapi.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private DtoUser user;
}
