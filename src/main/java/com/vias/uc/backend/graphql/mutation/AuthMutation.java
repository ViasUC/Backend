package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.LoginInput;
import com.vias.uc.backend.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthMutation {

    private final AuthService authService;

    public AuthMutation(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public Usuario login(@Argument("input") LoginInput input) {
        System.out.println(">>> LOGIN recibido: " + input.email());
        return authService.login(input.email(), input.password());
    }
}
