package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Integer> {
    
    /**
     * Busca todos los convenios de una empresa
     */
    List<Convenio> findByEmpresaId(Integer empresaId);
    
    /**
     * Busca todos los convenios de una empresa por estado
     */
    List<Convenio> findByEmpresaIdAndEstado(Integer empresaId, String estado);
    
    /**
     * Busca convenios vigentes de una empresa (estado 'Vigente' o 'Aprobado')
     */
    @Query("SELECT c FROM Convenio c WHERE c.empresaId = :empresaId " +
           "AND c.estado IN ('Vigente', 'Aprobado') " +
           "ORDER BY c.fechaCreacion DESC")
    List<Convenio> findConveniosVigentesByEmpresaId(@Param("empresaId") Integer empresaId);
    
    /**
     * Busca solicitudes de convenio de una empresa (estado 'Pendiente', 'En Revisión', 'Rechazado')
     */
    @Query("SELECT c FROM Convenio c WHERE c.empresaId = :empresaId " +
           "AND c.estado IN ('Pendiente', 'En Revisión', 'Rechazado') " +
           "ORDER BY c.fechaCreacion DESC")
    List<Convenio> findSolicitudesByEmpresaId(@Param("empresaId") Integer empresaId);
    
    /**
     * Busca un convenio específico de una empresa
     */
    Optional<Convenio> findByIdConvenAndEmpresaId(Integer idConven, Integer empresaId);
    
    /**
     * Cuenta convenios activos de una empresa
     */
    @Query("SELECT COUNT(c) FROM Convenio c WHERE c.empresaId = :empresaId " +
           "AND c.estado IN ('Vigente', 'Aprobado')")
    Long countConveniosActivosByEmpresaId(@Param("empresaId") Integer empresaId);
}
