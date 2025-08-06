package com.learnexa.learnexaapi.jwt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotNull(message = "İsim alanı null olamaz!")
    @NotEmpty(message = "Username alanı boş bırakılamaz!")
    @Size(min = 6, max = 30, message = "Username 6 ile 30 karakter arasında olmalıdır.")
    private String username;

    @Size(min = 6, message = "Password alanı en az 6 karakter olmalıdır.")
    private String password;
}
