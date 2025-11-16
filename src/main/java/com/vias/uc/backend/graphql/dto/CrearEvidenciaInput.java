package com.vias.uc.backend.graphql.dto;

import lombok.Data;

@Data
public class CrearEvidenciaInput {

    private Integer idPortafolio;
    private Integer idUsuario;  // obligatorio
    private String titulo;
    private String descripcion;
    private String tipo;
    private String recurso;
}
