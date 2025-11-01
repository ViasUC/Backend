package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// En AlumnoRepository.java
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    // Asumiendo que tu campo en la entidad Alumno se llama 'id_usuario'
    Optional<Alumno> findByIdUsuario(Integer id_usuario);
}
