package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    // Verifica si existe un Profesor por el ID del Usuario
    boolean existsByIdUsuario(Integer idUsuario);

    Profesor findByUsuario_IdUsuario(Integer usuarioId); // Accede al idUsuario de la entidad Usuario
}
