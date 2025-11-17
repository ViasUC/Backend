package com.vias.uc.backend.model.dto;


public record AlumnoInput(
        UsuarioInput usuario,
        String carrera,
        Integer semestre
) {
}