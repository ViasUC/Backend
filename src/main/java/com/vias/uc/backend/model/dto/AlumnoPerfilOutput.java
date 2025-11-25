package com.vias.uc.backend.model.dto;

public record AlumnoPerfilOutput(
        String nombre,
        String apellido,
        String email,
        String telefono,
        String ubicacion,
        String carrera,
        Integer semestre
) {}
