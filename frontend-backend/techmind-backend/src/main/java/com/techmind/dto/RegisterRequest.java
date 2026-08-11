package com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud de registro de nuevo usuario")
public class RegisterRequest {

    @Schema(description = "Correo electronico del usuario", example = "nuevo@techmind.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contrasena del usuario", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Nombre completo del usuario", example = "Juan Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}