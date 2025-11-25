package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UsuarioLiteRepository extends Repository<Usuario, Integer> {

    @Query(value = "select exists(select 1 from public.usuarios where id_usuario = :id)", nativeQuery = true)
    boolean existsByIdSimple(@Param("id") Integer id);

    @Query(value = "select rol_principal from public.usuarios where id_usuario = :id", nativeQuery = true)
    String rol(@Param("id") Integer id);

    //@Query(value = "select coalesce(activo,true) from public.usuarios where id_usuario = :id", nativeQuery = true)
    //Boolean activo(@Param("id") Integer id);

     //Opc. 1: siempre true (pruebas)
    @Query(value = "select true", nativeQuery = true)
    Boolean activo(@Param("id") Integer id);
}
