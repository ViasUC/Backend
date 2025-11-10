package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "portafolio", schema = "public")
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_portafolio")
    private Integer idPortafolio;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario; // dueño del portafolio

    // Getters & Setters
    public Integer getIdPortafolio() { return idPortafolio; }
    public void setIdPortafolio(Integer idPortafolio) { this.idPortafolio = idPortafolio; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}
