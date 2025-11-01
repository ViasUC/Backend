package com.vias.uc.backend.model.dto;


public record RegistroAlumnoInput(
        UsuarioRegistroInput usuario,  // <— ahora anidado
        String carrera,
        Integer semestre,
        String detalleAuditoria
) {}
