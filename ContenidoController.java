package com.techmind.controller;

import com.techmind.dto.ContenidoRequest;
import com.techmind.dto.ContenidoResponse;
import com.techmind.service.MlServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contenido")
@CrossOrigin(origins = "*")
public class ContenidoController {

    private final MlServiceClient mlServiceClient;

    public ContenidoController(MlServiceClient mlServiceClient) {
        this.mlServiceClient = mlServiceClient;
    }

    @PostMapping
    public ResponseEntity<ContenidoResponse> clasificar(@RequestBody ContenidoRequest request) {
        // Validación
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

        // Convertir String a double (ej: "0.95" → 0.95)
        try {
            response.setProbabilidad(Double.parseDouble(mlResponse.confianza()));
        } catch (NumberFormatException e) {
            response.setProbabilidad(0.0);
        }

        // Mapear palabras clave como información adicional
        response.setInformacionAdicional(mlResponse.palabrasClave());

        return ResponseEntity.ok(response);
    }
}