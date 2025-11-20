package com.vias.uc.backend.model.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostulanteDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String ubicacion;
    private String rolPrincipal;
    private Integer completitud;
    private LocalDateTime fechaPostulacion;
    private String estado;
}
