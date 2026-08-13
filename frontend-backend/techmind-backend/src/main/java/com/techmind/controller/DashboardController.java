package com.techmind.controller;

import com.techmind.dto.DashboardMetricsResponse;
import com.techmind.service.MlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Dashboard y Métricas", description = "Endpoints para consultar estadísticas de uso y estado de la plataforma")
public class DashboardController {

    private final MlService mlService;

    @GetMapping("/metrics")
    @Operation(summary = "Obtener métricas ejecutivas", description = "Devuelve KPIs generales de la aplicación como total de documentos, precisión y categoría dominante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = DashboardMetricsResponse.class)))
    })
    public ResponseEntity<DashboardMetricsResponse> getMetrics() {
        DashboardMetricsResponse metrics = mlService.obtenerMetricas();
        return ResponseEntity.ok(metrics);
    }
}
