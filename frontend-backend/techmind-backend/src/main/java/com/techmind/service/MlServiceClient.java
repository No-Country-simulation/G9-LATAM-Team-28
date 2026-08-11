package com.techmind.service;

import com.techmind.dto.MlHealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class MlServiceClient {

    @Value("${ml.service.url:http://localhost:8000}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public MlHealthResponse checkHealth() {
        try {
            ResponseEntity<MlHealthResponse> response = restTemplate.getForEntity(
                    mlServiceUrl + "/health",
                    MlHealthResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
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
                throw new RuntimeException("Respuesta vacia del servicio ML");
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
            throw new RuntimeException("Error leyendo archivo adjunto: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el servicio ML en " + mlServiceUrl + ": " + e.getMessage(), e);
        }
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