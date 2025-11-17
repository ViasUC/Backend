package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.repository.OportunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OportunidadService {

    @Autowired
    private OportunidadRepository oportunidadRepository;


    public List<Oportunidad> porCreador(Long creadorId) {
        return oportunidadRepository.findAllByCreadorId(creadorId);
    }

}
