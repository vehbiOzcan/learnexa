package com.learnexa.learnexaapi.jwt.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class JwtUtils {

    public String getUserNameFromSecurityContextHolder() {
        Object principals = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principals instanceof UserDetails) {
            return ((UserDetails)principals).getUsername();
        }
        return "username";
    }


}
