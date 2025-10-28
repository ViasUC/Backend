package com.vias.uc.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class DashboardController {

    @GetMapping("/api/v1/dashboard/summary")
    public Map<String, Object> getDashboardSummary() {
        return Map.of(
            "perfilCompletado", 75,
            "oportunidades", 10,
            "postulaciones", 2,
            "mensajesNuevos", 1
        );
    }
}

