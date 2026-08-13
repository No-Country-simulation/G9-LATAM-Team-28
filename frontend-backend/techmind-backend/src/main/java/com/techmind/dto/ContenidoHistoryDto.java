package com.techmind.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ContenidoHistoryDto(
        Long id,
        String titulo,
        String texto,
        String categoria,
        String confianza,
        List<String> palabrasClave,
        LocalDateTime createdAt
) {}
