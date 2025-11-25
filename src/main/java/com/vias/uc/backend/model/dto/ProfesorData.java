package com.vias.uc.backend.model.dto;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.Profesor;

public record ProfesorData(
        Usuario usuario,
        Profesor profesor
) {
}
