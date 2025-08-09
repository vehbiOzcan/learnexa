package com.learnexa.learnexaapi.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Email alanı null olamaz!")
    @NotEmpty(message = "Email alanı boş bırakılamaz!")
    private String email;

    @NotNull(message = "İsim alanı null olamaz!")
    @NotEmpty(message = "İsim alanı boş bırakılamaz!")
    @Size(min = 2, max = 50, message = "İsim 2 ile 50 karakter arasında olmalıdır.")
    private String fullname;

    @Size(min = 6, message = "Password alanı en az 6 karakter olmalıdır.")
    private String password;
}
