package com.techmind.controller;

import com.techmind.dto.DashboardMetricsResponse;
import com.techmind.entity.User;
import com.techmind.repository.UserRepository;
import com.techmind.service.MlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard y Métricas", description = "Endpoints para consultar estadísticas de uso y estado de la plataforma")
public class DashboardController {

    private final MlService mlService;
    private final UserRepository userRepository;

    public DashboardController(MlService mlService, UserRepository userRepository) {
        this.mlService = mlService;
        this.userRepository = userRepository;
    }

    private Long getUserIdFromSecurity() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
                String email = userDetails.getUsername();
                return userRepository.findByEmail(email).map(User::getId).orElse(null);
            }
        } catch (Exception ignored) {}
        return null;
    }

    @GetMapping("/metrics")
    @Operation(summary = "Obtener métricas ejecutivas", description = "Devuelve KPIs del usuario autenticado: total de documentos, precisión y categoría dominante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = DashboardMetricsResponse.class)))
    })
    public ResponseEntity<DashboardMetricsResponse> getMetrics() {
        Long userId = getUserIdFromSecurity();
        DashboardMetricsResponse metrics = mlService.obtenerMetricas(userId);
        return ResponseEntity.ok(metrics);
    }
}
