package com.vias.uc.backend.service.impl;

import com.vias.uc.backend.service.AuthDocente;
import com.vias.uc.backend.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceStub implements AuthDocente {

    @Override
    public Integer getUserId() {
        // valor fijo de prueba (cambia por el id del usuario logueado real cuando tengas auth JWT)
        return 10;  //10: profesor
    }
}
