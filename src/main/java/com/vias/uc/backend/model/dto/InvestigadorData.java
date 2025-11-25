package com.vias.uc.backend.model.dto;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.Investigador;

public record InvestigadorData(
        Usuario usuario,
        Investigador investigador
) {
}
