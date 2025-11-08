package com.vias.uc.backend.service.impl;

import com.vias.uc.backend.repository.UsuarioLiteRepository;
import com.vias.uc.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioLiteRepository repo;

    @Override
    public boolean exists(Integer id) {
        return repo.existsByIdSimple(id);
    }

    @Override
    public String rol(Integer id) {
        return repo.rol(id);
    }

    @Override
    public boolean activo(Integer id) {
        Boolean v = repo.activo(id);
        return v == null ? true : v;
    }
}
