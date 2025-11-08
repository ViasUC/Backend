package com.vias.uc.backend.service.impl;

import com.vias.uc.backend.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceStub implements AuthService {

    @Override
    public Integer getUserId() {
        // valor fijo de prueba (cambia por el id del usuario logueado real cuando tengas auth JWT)
        return 10;  //10: profesor
    }
}
