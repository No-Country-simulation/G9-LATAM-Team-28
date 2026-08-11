package com.techmind.controller;

import com.techmind.dto.ContenidoRequest;
import com.techmind.dto.ContenidoResponse;
import com.techmind.service.MlServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contenido")
@CrossOrigin(origins = "*")
@Tag(name = "Clasificacion de Contenido", description = "Endpoints para la clasificacion y analisis de texto con Machine Learning")
public class ContenidoController {

    private final MlServiceClient mlServiceClient;

    public ContenidoController(MlServiceClient mlServiceClient) {
        this.mlServiceClient = mlServiceClient;
    }

    @PostMapping
    @Operation(summary = "Clasificar contenido", description = "Analiza el titulo y texto proporcionados utilizando el servicio de Machine Learning para obtener su categoria, probabilidad y palabras clave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido clasificado exitosamente",
                    content = @Content(schema = @Schema(implementation = ContenidoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Texto nulo o vacio",
                    content = @Content)
    })
    public ResponseEntity<ContenidoResponse> clasificar(@RequestBody ContenidoRequest request) {
        // Validacion
        if (request.getTexto() == null || request.getTexto().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Llamar al servicio ML (nombre correcto: classify, con 2 argumentos)
        MlServiceClient.MlClassificationResponse mlResponse = mlServiceClient.classify(
                request.getTitulo(),
                request.getTexto()
        );

        // Mapear respuesta del ML al DTO de salida usando setters
        ContenidoResponse response = new ContenidoResponse();
        response.setCategoria(mlResponse.categoria());

        // Convertir String a double (ej: "0.95" -> 0.95)
        try {
            response.setProbabilidad(Double.parseDouble(mlResponse.confianza()));
        } catch (NumberFormatException e) {
            response.setProbabilidad(0.0);
        }

        // Mapear palabras clave como informacion adicional
        response.setInformacionAdicional(mlResponse.palabrasClave());

        return ResponseEntity.ok(response);
    }
}