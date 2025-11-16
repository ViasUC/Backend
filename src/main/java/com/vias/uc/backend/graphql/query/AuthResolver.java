package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.service.AuthService;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {

    public AuthResolver(AuthService authService) {
    }


    // Clase interna o registro (puede ir también en su propio archivo)
    public record LoginInput(String email, String password) {}
}
