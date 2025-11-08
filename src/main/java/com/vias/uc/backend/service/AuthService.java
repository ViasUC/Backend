package com.vias.uc.backend.service;

public interface AuthService {
    Integer getUserId(); // devuelve el id_usuario del autenticado (JWT/SecurityContext)
}
