package com.vias.uc.backend.model.dto;

public record UsuarioInput(
        String nombre,
        String apellido,
        String email,
        String telefono,
        String ubicacion
) {}