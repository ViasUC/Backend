package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // AÑADE ESTE MÉTODO:
    @Modifying
    @Query("UPDATE Usuario u SET " +
            "u.nombre = COALESCE(:nombre, u.nombre), " +
            "u.apellido = COALESCE(:apellido, u.apellido), " +
            "u.email = COALESCE(:email, u.email) " +
            "WHERE u.idUsuario = :id")
    void actualizarDatosUsuario(
            @Param("id") Integer id,
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("email") String email
    );
}
