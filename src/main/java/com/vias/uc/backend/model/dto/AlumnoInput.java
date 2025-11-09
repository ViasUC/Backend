package com.vias.uc.backend.model.dto;


public record AlumnoInput(
        UsuarioInput usuario,
        String carrera,
        Integer semestre
) {
    // Si usas una clase normal:
    /*
    private UsuarioInput usuario;
    private String carrera;
    private Integer semestre;
    // ... (constructores, getters y setters) ...
    */
}