package com.securitytest.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioLoginDTO {

    @NotBlank(message = "Nombre field cannot be empty!")
    private String userName;

    @NotBlank(message = "Password field cannot be empty!")
    private String password;

}
