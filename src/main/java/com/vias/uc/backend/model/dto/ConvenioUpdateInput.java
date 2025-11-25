package com.vias.uc.backend.model.dto;

/**
 * DTO para actualizar datos de un convenio vigente
 */
public class ConvenioUpdateInput {
    
    private String responsables;
    private String observaciones;

    // Constructores
    public ConvenioUpdateInput() {
    }

    // Getters y Setters
    public String getResponsables() {
        return responsables;
    }

    public void setResponsables(String responsables) {
        this.responsables = responsables;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
