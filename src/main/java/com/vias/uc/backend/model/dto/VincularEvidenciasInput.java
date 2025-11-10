package com.vias.uc.backend.model.dto;

import java.util.List;

public record VincularEvidenciasInput(
        Integer idPostulacion,
        List<Integer> idsEvidencias
) {}
