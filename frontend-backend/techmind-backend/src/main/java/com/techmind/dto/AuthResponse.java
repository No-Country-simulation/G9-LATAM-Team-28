package com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta de autenticacion con token JWT")
public class AuthResponse {

    @Schema(description = "Token JWT de acceso generado", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Correo electronico del usuario autenticado", example = "usuario@techmind.com")
    private String email;

    @Schema(description = "Nombre del usuario autenticado", example = "Juan Perez")
    private String nombre;
}