package com.techmind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta del estado de salud del servicio de Machine Learning")
public record MlHealthResponse(

    @Schema(description = "Estado general del servicio", example = "healthy")
    String status,

    @JsonProperty("model_loaded")
    @Schema(description = "Indica si el modelo ML se encuentra cargado en memoria", example = "true")
    boolean modelLoaded,

    @Schema(description = "Version actual de la API del servicio ML", example = "2.0.0")
    String version
) {}
