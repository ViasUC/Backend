package com.vias.uc.backend.graphql.dto;

import lombok.Data;

@Data
public class GuardarPortafolioInput {

    private Integer idUsuario;     // obligatorio
    private String descripcion;
    private String skills;
    private Boolean visibilidad;
}
