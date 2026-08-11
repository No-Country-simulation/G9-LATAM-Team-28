package com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Respuesta del analisis y clasificacion de contenido")
public class ContenidoResponse {

    @Schema(description = "Categoria asignada al contenido por el modelo de Machine Learning", example = "Tecnologia")
    private String categoria;

    @Schema(description = "Probabilidad de certeza de la clasificacion (entre 0.0 y 1.0)", example = "0.95")
    private double probabilidad;

    @Schema(description = "Lista de palabras clave extraidas del texto", example = "[\"inteligencia artificial\", \"tecnologia\", \"algoritmo\"]")
    private List<String> informacionAdicional;

    // Constructor vacio (necesario para Jackson)
    public ContenidoResponse() {}

    // Getters y Setters
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public List<String> getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(List<String> informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }
}
