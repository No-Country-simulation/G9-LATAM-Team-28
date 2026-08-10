package com.techmind.service;

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

    public MlServiceClient.MlClassificationResponse clasificarYGuardar(
            String titulo,
            String texto,
            MultipartFile archivo,
            Long userId
    ) {
        // 1. Llamar al microservicio Python
        MlServiceClient.MlClassificationResponse resultado =
                mlClient.classify(titulo, texto, archivo);

        // 2. Guardar en PostgreSQL
        Contenido contenido = new Contenido();
        contenido.setTitulo(titulo != null ? titulo : "Sin título");
        contenido.setTexto(texto);
        contenido.setCategoria(resultado.categoria());
        contenido.setConfianza(resultado.confianza());

        if (resultado.palabrasClave() != null) {
            contenido.setPalabrasClave(String.join(", ", resultado.palabrasClave()));
        }

        contenido.setUserId(userId);
        contenidoRepository.save(contenido);

        return resultado;
    }
}