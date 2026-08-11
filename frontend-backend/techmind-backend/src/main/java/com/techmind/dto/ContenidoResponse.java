package com.techmind.dto;

import java.util.List;

public class ContenidoResponse {
    private String categoria;
    private double probabilidad;
    private List<String> informacionAdicional;

    // Constructor vacío (necesario para Jackson)
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
