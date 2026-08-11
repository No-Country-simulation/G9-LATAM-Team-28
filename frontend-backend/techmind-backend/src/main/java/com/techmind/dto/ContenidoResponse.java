package com.techmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Respuesta del analisis y clasificacion de contenido")
public class ContenidoResponse {

    @Schema(description = "Categoria asignada al contenido por el modelo de Machine Learning", example = "Desarrollo Web")
    private String categoria;

    @Schema(description = "Porcentaje de confianza devuelto por el modelo ML", example = "95%")
    private String confianza;

    @Schema(description = "Probabilidad numerica de certeza de la clasificacion (entre 0.0 y 1.0)", example = "0.95")
    private double probabilidad;

    @Schema(description = "Lista de palabras clave de mayor relevancia TF-IDF extraidas del documento", example = "[\"microservicios\", \"API\", \"seguridad\"]")
    private List<String> palabrasClave;

    @Schema(description = "Informacion adicional o alias de palabras clave para compatibilidad", example = "[\"microservicios\", \"API\", \"seguridad\"]")
    private List<String> informacionAdicional;

    public ContenidoResponse() {}

    public ContenidoResponse(String categoria, String confianza, double probabilidad, List<String> palabrasClave) {
        this.categoria = categoria;
        this.confianza = confianza;
        this.probabilidad = probabilidad;
        this.palabrasClave = palabrasClave;
        this.informacionAdicional = palabrasClave;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getConfianza() {
        return confianza;
    }

    public void setConfianza(String confianza) {
        this.confianza = confianza;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public List<String> getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(List<String> palabrasClave) {
        this.palabrasClave = palabrasClave;
        this.informacionAdicional = palabrasClave;
    }

    public List<String> getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(List<String> informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
        if (this.palabrasClave == null) {
            this.palabrasClave = informacionAdicional;
        }
    }
}
