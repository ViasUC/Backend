package com.vias.uc.backend.model.dto;

import com.vias.uc.backend.model.enums.RolUsuario;

public record UsuarioRegistroInput(
        String nombre,
        String apellido,
        String email,
        String telefono,
        String ubicacion,
        String password,
        RolUsuario rolPrincipal,   // enum en el DTO
        Integer completitud
) {}