package com.learnexa.learnexaapi.dto;

import com.learnexa.learnexaapi.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser {
    private Long id;
    private String username;
    private Role role;
}
