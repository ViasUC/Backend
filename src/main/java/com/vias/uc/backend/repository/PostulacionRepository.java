package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Postulacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    @Query("""
        SELECT p FROM Postulacion p
        WHERE p.oportunidad.idOportunidad = :oppId
          AND (:estado IS NULL OR p.estado = :estado)
          AND (
                :q IS NULL
                OR LOWER(p.usuario.nombre)   LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(p.usuario.apellido) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(p.usuario.email)    LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(p.mensaje)          LIKE LOWER(CONCAT('%', :q, '%'))
          )
    """)
    Page<Postulacion> buscarPorOportunidad(@Param("oppId") Long oportunidadId,
                                           @Param("estado") Postulacion.EstadoPostulacion estado,
                                           @Param("q") String q,
                                           Pageable pageable);
}
