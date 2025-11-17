package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.EmpresaUsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
