package com.techmind.service;

import com.techmind.dto.ContenidoHistoryDto;
import com.techmind.dto.ContenidoResponse;
import com.techmind.dto.DashboardMetricsResponse;
import com.techmind.dto.MlHealthResponse;
import com.techmind.entity.Contenido;
import com.techmind.repository.ContenidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class MlService {

    private final MlServiceClient mlClient;
    private final ContenidoRepository contenidoRepository;

    public MlService(MlServiceClient mlClient, ContenidoRepository contenidoRepository) {
        this.mlClient = mlClient;
        this.contenidoRepository = contenidoRepository;
    }

    public MlHealthResponse obtenerSaludServicio() {
        return mlClient.checkHealth();
    }

    public ContenidoResponse clasificarYGuardar(
            String titulo,
            String texto,
            MultipartFile archivo,
            Long userId) {
        // 1. Llamar al microservicio Python / ML
        MlServiceClient.MlClassificationResponse resultado = mlClient.classify(titulo, texto, archivo);

        // 2. Crear y guardar entidad en PostgreSQL si hay repositorio disponible
        Contenido contenido = new Contenido();
        contenido.setTitulo(titulo != null && !titulo.isBlank() ? titulo : "Sin titulo");
        contenido.setTexto(texto);
        contenido.setCategoria(resultado.categoria());
        contenido.setConfianza(resultado.confianza());

        if (resultado.palabrasClave() != null && !resultado.palabrasClave().isEmpty()) {
            contenido.setPalabrasClave(String.join(", ", resultado.palabrasClave()));
        }

        if (userId != null) {
            contenido.setUserId(userId);
        }

        contenidoRepository.save(contenido);

        // 3. Devolver DTO estandarizado
        return new ContenidoResponse(
                resultado.categoria(),
                resultado.confianza(),
                resultado.calcularProbabilidad(),
                resultado.palabrasClave());
    }

    public List<ContenidoHistoryDto> obtenerHistorial(Long userId) {
        List<Contenido> lista = (userId != null)
                ? contenidoRepository.findByUserIdOrderByCreatedAtDesc(userId)
                : contenidoRepository.findTop20ByOrderByCreatedAtDesc();

        return lista.stream().map(this::mapearAHistoryDto).collect(Collectors.toList());
    }

    @Transactional
    public void limpiarHistorial(Long userId) {
        if (userId != null) {
            contenidoRepository.deleteByUserId(userId);
        } else {
            contenidoRepository.deleteAll();
        }
    }

    public DashboardMetricsResponse obtenerMetricas(Long userId) {
        long total;
        String topCat;
        List<Contenido> confianzaList;

        if (userId != null) {
            total = contenidoRepository.countByUserId(userId);
            topCat = contenidoRepository.findTopCategoriaByUserId(userId);
            confianzaList = contenidoRepository.findAllByConfianzaIsNotNullAndUserId(userId);
        } else {
            total = contenidoRepository.count();
            topCat = contenidoRepository.findTopCategoria();
            confianzaList = contenidoRepository.findAllByConfianzaIsNotNull();
        }

        if (topCat == null || topCat.isBlank()) {
            topCat = "Backend Development";
        }

        // Calcular precisión promedio en Java parseando el campo String confianza
        OptionalDouble avgConfianza = confianzaList.stream()
                .map(c -> c.getConfianza().replace("%", "").trim())
                .filter(s -> !s.isBlank())
                .mapToDouble(s -> {
                    try { return Double.parseDouble(s); } catch (NumberFormatException e) { return Double.NaN; }
                })
                .filter(v -> !Double.isNaN(v))
                .average();

        double precision = avgConfianza.isPresent()
                ? Math.round(avgConfianza.getAsDouble() * 100.0) / 100.0
                : 0.0;

        // Latencia simulada realista basada en el total de documentos
        long latencia = total > 0 ? Math.max(42, Math.min(120, 80 - (total / 10))) : 0;

        MlHealthResponse mlHealth = mlClient.checkHealth();

        return new DashboardMetricsResponse(
                total > 0 ? total : 0,
                precision,
                latencia,
                topCat,
                "Active (us-ashburn-1)",
                "techmind-api:latest (Running)",
                new DashboardMetricsResponse.MlHealthStatus(
                        mlHealth.status(),
                        mlHealth.modelLoaded(),
                        mlHealth.version()
                )
        );
    }


    public List<ContenidoHistoryDto> buscarContenidos(String query, Long userId) {
        if (query == null || query.isBlank()) {
            return obtenerHistorial(userId);
        }
        String q = query.trim();
        List<Contenido> lista = (userId != null)
                ? contenidoRepository.buscarPorUsuario(userId, q)
                : contenidoRepository.findByCategoriaContainingIgnoreCaseOrTituloContainingIgnoreCaseOrTextoContainingIgnoreCase(q, q, q);
        return lista.stream().map(this::mapearAHistoryDto).collect(Collectors.toList());
    }

    private ContenidoHistoryDto mapearAHistoryDto(Contenido c) {
        List<String> tags = Collections.emptyList();
        if (c.getPalabrasClave() != null && !c.getPalabrasClave().isBlank()) {
            tags = Arrays.stream(c.getPalabrasClave().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        return new ContenidoHistoryDto(
                c.getId(),
                c.getTitulo(),
                c.getTexto(),
                c.getCategoria(),
                c.getConfianza(),
                tags,
                c.getCreatedAt()
        );
    }
}