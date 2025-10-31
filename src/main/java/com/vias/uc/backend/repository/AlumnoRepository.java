package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {}
