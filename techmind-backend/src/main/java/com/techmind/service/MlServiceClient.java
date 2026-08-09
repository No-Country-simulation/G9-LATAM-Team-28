package com.techmind.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class MlServiceClient {

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ MÉTODO NUEVO: sobrecarga para llamar sin archivo
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
                throw new RuntimeException("Respuesta vacía del servicio ML");
            }

            @SuppressWarnings("unchecked")
            List<String> palabrasClave = (List<String>) result.get("palabras_clave");

            @SuppressWarnings("unchecked")
            List<Map<String, String>> recomendaciones = (List<Map<String, String>>) result.get("recomendaciones");

            return new MlClassificationResponse(
                    (String) result.get("categoria"),
                    (String) result.get("confianza"),
                    palabrasClave,
                    recomendaciones
            );

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con ML service: " + e.getMessage(), e);
        }
    }

    private static class MultipartFileResource extends ByteArrayResource {
        private final String filename;

        MultipartFileResource(MultipartFile file) throws IOException {
            super(file.getBytes());
            this.filename = file.getOriginalFilename();
        }

        @Override
        public String getFilename() {
            return filename;
        }
    }

    public record MlClassificationResponse(
            String categoria,
            String confianza,
            List<String> palabrasClave,
            List<Map<String, String>> recomendaciones
    ) {}
}