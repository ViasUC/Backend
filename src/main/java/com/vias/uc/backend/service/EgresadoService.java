package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Egresado;
import com.vias.uc.backend.model.dto.EgresadoInput;
import com.vias.uc.backend.repository.EgresadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EgresadoService {

    private final EgresadoRepository egresadoRepository;

    public EgresadoService(EgresadoRepository egresadoRepository) {
        this.egresadoRepository = egresadoRepository;
    }

    @Transactional
    public Egresado actualizarEgresado(Integer idUsuario, EgresadoInput input) {

        System.out.println("=== [DEBUG] Actualizando EGRESADO idUsuario=" + idUsuario + " ===");

        Egresado egresado = egresadoRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Egresado no encontrado con id_usuario: " + idUsuario));

        if (input.anioEgreso() != null) {
            egresado.setAnioEgreso(input.anioEgreso());
        }

        if (input.titulo() != null) {
            egresado.setTitulo(input.titulo());
        }

        Egresado actualizado = egresadoRepository.save(egresado);

        System.out.println("=== [DEBUG] Egresado actualizado: " +
                actualizado.getAnioEgreso() + " - " + actualizado.getTitulo() + " ===");

        return actualizado;
    }
}
