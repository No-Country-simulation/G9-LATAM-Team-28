package com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud de inicio de sesion")
public class LoginRequest {

    @Schema(description = "Correo electronico del usuario", example = "usuario@techmind.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contrasena del usuario", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}