package com.vias.uc.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private static final String TOKEN = "mock.jwt.token";
    // Usuarios válidos en memoria (email -> password)
    private static final Map<String, String> USERS = Map.of(
        "test@uca.edu.py", "1234",
        "admin@uca.edu.py", "admin123"
    );

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "");
        String password = body.getOrDefault("password", "");

        String expected = USERS.get(email);
        if (expected != null && expected.equals(password)) {
            return ResponseEntity.ok(Map.of(
                "accessToken", TOKEN,
                "user", Map.of(
                    "id", email.equals("test@uca.edu.py") ? "u1" : "u2",
                    "nombre", "Usuario " + email,
                    "email", email,
                    "rol", "EXALUMNO"
                )
            ));
        }
        return ResponseEntity.status(401).body(Map.of(
            "code", "UNAUTHORIZED",
            "message", "Credenciales inválidas"
        ));
    }
}
