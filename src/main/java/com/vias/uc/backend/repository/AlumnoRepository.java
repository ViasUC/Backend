package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    // Usa la firma estándar de JpaRepository (Long). No redefinas con Integer.
    @EntityGraph(attributePaths = {"usuario"})
    Optional<Alumno> findById(Long id);

    boolean existsById(Long id);

    // Opcional: atajos útiles si querés buscar por el id del usuario
    // Optional<Alumno> findByUsuario_IdUsuario(Long idUsuario);
    // boolean existsByUsuario_IdUsuario(Long idUsuario);
}
