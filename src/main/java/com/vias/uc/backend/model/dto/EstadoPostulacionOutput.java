package com.vias.uc.backend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EstadoPostulacionOutput(
        Integer idPostulacion,
        String estado,
        LocalDateTime fechaPostulacion,
        String tituloOportunidad,
        List<Integer> evidencias
) {}
