package com.vias.uc.backend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostulacionOutput(
        Integer idPostulacion,
        String estado,
        String tituloOportunidad,
        LocalDateTime fechaPostulacion,
        List<Integer> evidencias
) {}
