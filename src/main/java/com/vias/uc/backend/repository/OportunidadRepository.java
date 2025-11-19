package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Oportunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <--- IMPORTANTE
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//               Añade JpaSpecificationExecutor
//                      vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
public interface OportunidadRepository extends JpaRepository<Oportunidad, Integer>, JpaSpecificationExecutor<Oportunidad> {

    @Query("SELECT o FROM Oportunidad o WHERE o.idCreador = :creadorId")
    List<Oportunidad> findAllByCreadorId(@Param("creadorId") Long creadorId);

    List<Oportunidad> findByEstado(String estado);

}