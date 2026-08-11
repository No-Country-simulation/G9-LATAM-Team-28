package com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud para clasificar contenido")
public class ContenidoRequest {

    @Schema(description = "Titulo del contenido o articulo", example = "Introduccion a la Inteligencia Artificial")
    private String titulo;

    @Schema(description = "Texto completo del contenido a analizar", example = "La inteligencia artificial es una rama de la informatica...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String texto;
}