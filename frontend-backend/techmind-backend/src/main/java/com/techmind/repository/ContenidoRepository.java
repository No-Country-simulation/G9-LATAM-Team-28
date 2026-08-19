package com.techmind.repository;

import com.techmind.entity.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    List<Contenido> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Contenido> findTop20ByOrderByCreatedAtDesc();
    void deleteByUserId(Long userId);

    @Query("SELECT c.categoria FROM Contenido c WHERE c.categoria IS NOT NULL GROUP BY c.categoria ORDER BY COUNT(c) DESC LIMIT 1")
    String findTopCategoria();

    List<Contenido> findAllByConfianzaIsNotNull();

    List<Contenido> findByCategoriaContainingIgnoreCaseOrTituloContainingIgnoreCaseOrTextoContainingIgnoreCase(
            String categoria, String titulo, String texto);
}