package com.techmind.repository;

import com.techmind.entity.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
    List<Contenido> findByUserIdOrderByCreatedAtDesc(Long userId);
}