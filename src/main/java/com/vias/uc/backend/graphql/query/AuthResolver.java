package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {

    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public Usuario login(@Argument("input") LoginInput input) {
        return authService.login(input.email(), input.password());
    }

    // Clase interna o registro (puede ir también en su propio archivo)
    public record LoginInput(String email, String password) {}
}
