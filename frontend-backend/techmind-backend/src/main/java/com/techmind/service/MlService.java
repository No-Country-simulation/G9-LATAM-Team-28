package com.techmind.service;

import com.techmind.dto.ContenidoResponse;
import com.techmind.dto.MlHealthResponse;
import com.techmind.entity.Contenido;
import com.techmind.repository.ContenidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}