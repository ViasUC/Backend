package com.vias.uc.backend.model.dto;

import java.time.LocalDate;

/**
 * DTO para entrada de datos al solicitar un convenio
 */
public class ConvenioInput {
    
    private String institucion;
    private String descripcion;
    private LocalDate fechaIni;
    private LocalDate fechaFin;
    private String responsables;
    private String duracion;
    private String cantHoras;
    private String objetivos;
    private String beneficios;
    private String requisitos;
    private String documentoAdjunto;

    // Constructores
    public ConvenioInput() {
    }

    // Getters y Setters
    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(LocalDate fechaIni) {
        this.fechaIni = fechaIni;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getResponsables() {
        return responsables;
    }

    public void setResponsables(String responsables) {
        this.responsables = responsables;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getCantHoras() {
        return cantHoras;
    }

    public void setCantHoras(String cantHoras) {
        this.cantHoras = cantHoras;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getDocumentoAdjunto() {
        return documentoAdjunto;
    }

    public void setDocumentoAdjunto(String documentoAdjunto) {
        this.documentoAdjunto = documentoAdjunto;
    }
}
