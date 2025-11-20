package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.repository.PostulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostulacionService {

    @Autowired
    private PostulacionRepository postulacionRepository;

    public boolean actualizarEstado(int idPostulante, int idOportunidad, String estado) {
        Postulacion p = postulacionRepository.findByIdPostulanteAndIdOportunidad(idPostulante, idOportunidad);

        if (p == null) return false;

        p.setEstado(estado);
        postulacionRepository.save(p);

        return true;
    }
}
