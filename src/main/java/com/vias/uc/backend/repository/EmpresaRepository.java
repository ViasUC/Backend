package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    /**
     * Busca la empresa activa de un usuario mediante JOIN con empresa_usuario
     */
    @Query("SELECT e FROM Empresa e " +
           "INNER JOIN EmpresaUsuario eu ON e.idEmpresa = eu.empresa " +
           "WHERE eu.usuario = :usuarioId AND eu.activo = true")
    Optional<Empresa> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca una empresa por email
     */
    Optional<Empresa> findByEmail(String email);
}
