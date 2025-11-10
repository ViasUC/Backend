package com.vias.uc.backend.model.dto;

public record CrearPostulacionInput(
        Integer idPostulante,
        Integer idOportunidad,
        String motivo // opcional
) {}
