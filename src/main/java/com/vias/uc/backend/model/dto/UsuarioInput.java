package com.vias.uc.backend.model.dto;

// Usamos 'record' que es más simple que 'class' para DTOs.
// Si no usas Java 16+, puedes usar una clase normal con getters/setters.
public record UsuarioInput(
        String nombre,
        String apellido,
        String email
) {
    // Si usas una clase normal:
    /*
    private String nombre;
    private String apellido;
    private String email;
    // ... (constructores, getters y setters) ...
    */
}