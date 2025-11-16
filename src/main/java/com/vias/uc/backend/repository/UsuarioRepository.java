package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Obtiene todas las ubicaciones únicas de usuarios
     */
    @Query("SELECT DISTINCT u.ubicacion FROM Usuario u WHERE u.ubicacion IS NOT NULL ORDER BY u.ubicacion")
    List<String> findDistinctUbicaciones();
}
