package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // usado por AlumnoService.crearAlumno(...)
    Optional<Usuario> findByEmail(String email);

    // Si querés forzar búsqueda case-insensitive en Postgres, podés usar esta alternativa:
    // @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    // Optional<Usuario> findByEmailIgnoreCase(@Param("email") String email);
}
