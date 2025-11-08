package com.vias.uc.backend.service;

public interface UsuarioService {
    boolean exists(Integer id);
    String rol(Integer id);      // p.ej. "DOCENTE", "EMPRESARIO", "ALUMNO", etc.
    boolean activo(Integer id);  // true si está habilitado/activo
}
