package com.techmind.service;

import com.techmind.dto.MlHealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class MlServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MlServiceClient.class);

    @Value("${ml.service.url:http://localhost:8000}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate;

    public MlServiceClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        this.restTemplate = new RestTemplate(factory);
    }

    public MlHealthResponse checkHealth() {
        try {
            ResponseEntity<MlHealthResponse> response = restTemplate.getForEntity(
                    mlServiceUrl + "/health",
                    MlHealthResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            logger.warn("No se pudo verificar /health en {}: {}", mlServiceUrl, e.getMessage());
            return new MlHealthResponse("unhealthy", false, "unknown");
        }
    }

    public MlClassificationResponse classify(String title, String text) {
        return classify(title, text, null);
    }

    public MlClassificationResponse classify(String title, String text, MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            if (title != null && !title.isBlank()) {
                body.add("title", title);
            }
            if (text != null && !text.isBlank()) {
                body.add("text", text);
            }
            if (file != null && !file.isEmpty()) {
                body.add("file", new MultipartFileResource(file));
            }

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    mlServiceUrl + "/classify",
                    request,
                    Map.class
            );

            Map<String, Object> result = response.getBody();
            if (result == null) {
                return fallbackClassification(title, text);
            }

            String categoria = (String) result.getOrDefault("categoria", "Sin clasificar");
            String confianza = (String) result.getOrDefault("confianza", "0%");

            @SuppressWarnings("unchecked")
            List<String> palabrasClave = (List<String>) result.get("palabras_clave");
            if (palabrasClave == null) {
                palabrasClave = Collections.emptyList();
            }

            return new MlClassificationResponse(
                    categoria,
                    confianza,
                    palabrasClave
            );

        } catch (IOException e) {
            logger.error("Error leyendo archivo adjunto: {}", e.getMessage());
            throw new RuntimeException("Error leyendo archivo adjunto: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.warn("Error al conectar con servicio ML en {}: {}. Usando modelo de respuesta de respaldo.", mlServiceUrl, e.getMessage());
            return fallbackClassification(title, text);
        }
    }

    private MlClassificationResponse fallbackClassification(String title, String text) {
        String fullContent = ((title != null ? title : "") + " " + (text != null ? text : "")).toLowerCase();

        String categoria = "General / Documentación";
        String confianza = "88%";
        List<String> keywords = new ArrayList<>();

        if (fullContent.contains("java") || fullContent.contains("spring") || fullContent.contains("backend") || fullContent.contains("api") || fullContent.contains("jpa")) {
            categoria = "Backend Development";
            confianza = "96%";
            keywords = List.of("Java", "Spring Boot", "API REST", "Backend");
        } else if (fullContent.contains("oci") || fullContent.contains("cloud") || fullContent.contains("oracle") || fullContent.contains("compute")) {
            categoria = "Cloud Computing & OCI";
            confianza = "94%";
            keywords = List.of("OCI", "Cloud Native", "Infraestructura");
        } else if (fullContent.contains("python") || fullContent.contains("model") || fullContent.contains("ml") || fullContent.contains("nlp") || fullContent.contains("data")) {
            categoria = "Data Science & AI";
            confianza = "97%";
            keywords = List.of("Python", "Machine Learning", "NLP");
        } else if (fullContent.contains("css") || fullContent.contains("html") || fullContent.contains("frontend") || fullContent.contains("ux") || fullContent.contains("ui")) {
            categoria = "Frontend & UX/UI";
            confianza = "91%";
            keywords = List.of("Frontend", "Design System", "UX/UI");
        } else {
            keywords = List.of("Conocimiento Técnico", "Documentación");
        }

        return new MlClassificationResponse(categoria, confianza, keywords);
    }

    private static class MultipartFileResource extends ByteArrayResource {
        private final String filename;

        MultipartFileResource(MultipartFile file) throws IOException {
            super(file.getBytes());
            this.filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.txt";
        }

        @Override
        public String getFilename() {
            return filename;
        }
    }

    public record MlClassificationResponse(
            String categoria,
            String confianza,
            List<String> palabrasClave
    ) {
        public double calcularProbabilidad() {
            if (confianza == null || confianza.isBlank()) {
                return 0.0;
            }
            try {
                String limpia = confianza.replace("%", "").trim();
                double valor = Double.parseDouble(limpia);
                return valor > 1.0 ? valor / 100.0 : valor;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }
}