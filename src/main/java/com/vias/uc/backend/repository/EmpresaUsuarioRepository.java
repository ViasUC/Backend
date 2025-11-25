package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.EmpresaUsuarioId;
import com.vias.uc.backend.model.enums.RolEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaUsuarioRepository extends JpaRepository<EmpresaUsuario, EmpresaUsuarioId> {
    
    /**
     * Encuentra todas las relaciones empresa-usuario activas de un usuario
     */
    List<EmpresaUsuario> findByUsuarioAndActivoTrue(Long idUsuario);
    
    /**
     * Encuentra la relación entre una empresa y un usuario específico
     */
    Optional<EmpresaUsuario> findByEmpresaAndUsuario(Integer idEmpresa, Long idUsuario);
    
    /**
     * Encuentra todas las relaciones de una empresa
     */
    List<EmpresaUsuario> findByEmpresa(Integer idEmpresa);
    
    /**
     * Encuentra todas las relaciones activas de una empresa
     */
    List<EmpresaUsuario> findByEmpresaAndActivoTrue(Integer idEmpresa);
    
    /**
     * Encuentra todas las solicitudes pendientes de una empresa (activo=false)
     */
    List<EmpresaUsuario> findByEmpresaAndActivoFalse(Integer idEmpresa);
    
    /**
     * Verifica si un usuario tiene un rol específico en una empresa
     */
    @Query("SELECT CASE WHEN COUNT(eu) > 0 THEN true ELSE false END " +
           "FROM EmpresaUsuario eu " +
           "WHERE eu.empresa = :idEmpresa " +
           "AND eu.usuario = :idUsuario " +
           "AND eu.rolEnEmpresa = :rol " +
           "AND eu.activo = true")
    boolean existsByEmpresaAndUsuarioAndRolEnEmpresaAndActivoTrue(
        Integer idEmpresa, 
        Long idUsuario, 
        RolEmpresa rol
    );
    
    /**
     * Cuenta cuántos administradores activos tiene una empresa
     */
    @Query("SELECT COUNT(eu) FROM EmpresaUsuario eu " +
           "WHERE eu.empresa = :idEmpresa " +
           "AND eu.rolEnEmpresa = 'ADMINISTRADOR' " +
           "AND eu.activo = true")
    long countAdministradoresActivos(Integer idEmpresa);
}
