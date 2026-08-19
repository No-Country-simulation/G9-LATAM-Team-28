package com.techmind.repository;

import com.techmind.entity.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    List<Contenido> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Contenido> findTop20ByOrderByCreatedAtDesc();
    void deleteByUserId(Long userId);

    // ── métricas globales (admin / sin sesión) ──────────────────────────────
    @Query("SELECT c.categoria FROM Contenido c WHERE c.categoria IS NOT NULL GROUP BY c.categoria ORDER BY COUNT(c) DESC LIMIT 1")
    String findTopCategoria();

    List<Contenido> findAllByConfianzaIsNotNull();

    // ── métricas filtradas por usuario ──────────────────────────────────────
    long countByUserId(Long userId);

    @Query("SELECT c.categoria FROM Contenido c WHERE c.userId = :userId AND c.categoria IS NOT NULL GROUP BY c.categoria ORDER BY COUNT(c) DESC LIMIT 1")
    String findTopCategoriaByUserId(@Param("userId") Long userId);

    List<Contenido> findAllByConfianzaIsNotNullAndUserId(Long userId);

    // ── búsqueda global (sin filtro de usuario) ─────────────────────────────
    List<Contenido> findByCategoriaContainingIgnoreCaseOrTituloContainingIgnoreCaseOrTextoContainingIgnoreCase(
            String categoria, String titulo, String texto);

    // ── búsqueda filtrada por usuario ───────────────────────────────────────
    @Query("SELECT c FROM Contenido c WHERE c.userId = :userId AND (" +
           "UPPER(c.categoria) LIKE UPPER(CONCAT('%', :q, '%')) OR " +
           "UPPER(c.titulo)    LIKE UPPER(CONCAT('%', :q, '%')) OR " +
           "UPPER(c.texto)     LIKE UPPER(CONCAT('%', :q, '%')))")
    List<Contenido> buscarPorUsuario(@Param("userId") Long userId, @Param("q") String q);
}