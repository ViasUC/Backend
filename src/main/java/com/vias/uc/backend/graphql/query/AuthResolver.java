package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {

    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Verifica si un email está disponible para registro
     * @param email Email a verificar
     * @return true si está disponible, false si ya existe
     */
    @QueryMapping
    public Boolean verificarEmailDisponible(@Argument String email) {
        return authService.isEmailDisponible(email);
    }

    // Clase interna o registro (puede ir también en su propio archivo)
    public record LoginInput(String email, String password) {}
}
