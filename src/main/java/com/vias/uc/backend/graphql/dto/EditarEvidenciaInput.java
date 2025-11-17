package com.vias.uc.backend.graphql.dto;

import lombok.Data;

@Data
public class EditarEvidenciaInput {

    private Integer idEvidencia;    // obligatorio
    private Integer idUsuario;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String recurso;
}
