package com.vias.uc.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DashboardController {

    private static final String TOKEN = "mock.jwt.token";

    @GetMapping("/api/v1/dashboard/summary")
    public ResponseEntity<?> getDashboardSummary(
            @RequestHeader(value = "Authorization", required = false) String auth) {

        if (auth == null || !auth.equals("Bearer " + TOKEN)) {
            return ResponseEntity.status(401).body(Map.of(
                "code", "UNAUTHORIZED",
                "message", "Falta o es inválido el token"
            ));
        }

        return ResponseEntity.ok(Map.of(
            "perfilCompletado", 75,
            "oportunidades", 10,
            "postulaciones", 2,
            "mensajesNuevos", 1
        ));
    }
}
