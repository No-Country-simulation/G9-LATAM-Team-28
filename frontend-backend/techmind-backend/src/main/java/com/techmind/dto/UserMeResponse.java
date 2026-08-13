package com.techmind.dto;

public record UserMeResponse(
        Long id,
        String email,
        String nombre,
        String rol
) {}
