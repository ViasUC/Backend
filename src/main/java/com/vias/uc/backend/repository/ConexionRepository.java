package com.vias.uc.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vias.uc.backend.model.Conexion;

@Repository
public interface ConexionRepository extends JpaRepository<Conexion, Integer> {

    List<Conexion> findByIdUsuario1OrIdUsuario2(Integer idUsuario1, Integer idUsuario2);
    

}
