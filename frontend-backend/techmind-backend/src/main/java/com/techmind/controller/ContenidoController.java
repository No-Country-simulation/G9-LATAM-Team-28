package com.techmind.controller;

import com.techmind.dto.ContenidoRequest;
import com.techmind.dto.ContenidoResponse;
import com.techmind.dto.MlHealthResponse;
import com.techmind.service.MlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contenido")
@CrossOrigin(origins = "*")
@Tag(name = "Clasificacion de Contenido", description = "Endpoints para clasificar texto y archivos mediante el modelo de Machine Learning")
public class ContenidoController {

    private final MlService mlService;

    public ContenidoController(MlService mlService) {
        this.mlService = mlService;
    }

    @GetMapping("/ml-health")
    @Operation(summary = "Verificar estado del servicio ML", description = "Consulta la disponibilidad del microservicio de Machine Learning y el estado del modelo cargado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de salud obtenido exitosamente", content = @Content(schema = @Schema(implementation = MlHealthResponse.class)))
    })
    public ResponseEntity<MlHealthResponse> checkMlHealth() {
        MlHealthResponse health = mlService.obtenerSaludServicio();
        return ResponseEntity.ok(health);
    }

    @PostMapping
    @Operation(summary = "Clasificar contenido en formato JSON", description = "Analiza el titulo y texto enviados en un cuerpo JSON, utiliza el modelo NLP y guarda el resultado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido clasificado exitosamente", content = @Content(schema = @Schema(implementation = ContenidoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de solicitud invalido o sin contenido de texto", content = @Content)
    })
    public ResponseEntity<ContenidoResponse> clasificarJson(@RequestBody ContenidoRequest request) {
        if (request == null || (request.getTexto() == null || request.getTexto().trim().isEmpty())
                && (request.getTitulo() == null || request.getTitulo().trim().isEmpty())) {
            return ResponseEntity.badRequest().build();
        }

        ContenidoResponse response = mlService.clasificarYGuardar(
                request.getTitulo(),
                request.getTexto(),
                null,
                null);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/archivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Clasificar texto y/o archivo adjunto", description = "Procesa texto directo y/o archivos adjuntos (TXT, PDF, DOCX, imagenes) mediante multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo y contenido clasificados exitosamente", content = @Content(schema = @Schema(implementation = ContenidoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida o sin archivo/texto adjunto", content = @Content)
    })
    public ResponseEntity<ContenidoResponse> clasificarArchivo(
            @Parameter(description = "Titulo opcional del contenido") @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "Texto opcional a clasificar") @RequestParam(value = "text", required = false) String text,
            @Parameter(description = "Archivo adjunto a analizar (TXT, PDF, DOCX, imagen)") @RequestParam(value = "file", required = false) MultipartFile file) {
        if ((file == null || file.isEmpty())
                && (text == null || text.trim().isEmpty())
                && (title == null || title.trim().isEmpty())) {
            return ResponseEntity.badRequest().build();
        }

        ContenidoResponse response = mlService.clasificarYGuardar(
                title,
                text,
                file,
                null);

        return ResponseEntity.ok(response);
    }
}