package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.graphql.dto.GuardarPortafolioInput;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.repository.PortafolioRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.AuditoriaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Controller
public class PortafolioMutation {

    private final PortafolioRepository portRepo;
    private final UsuarioRepository userRepo;
    private final AuditoriaService auditoriaService;


    public PortafolioMutation(PortafolioRepository portRepo, UsuarioRepository userRepo, AuditoriaService auditoriaService) {
        this.portRepo = portRepo;
        this.userRepo = userRepo;
        this.auditoriaService = auditoriaService;
    }


    @MutationMapping
    @Transactional
    public Portafolio crearPortafolio(@Argument GuardarPortafolioInput input) {

        // Validación: un usuario no puede tener más de un portafolio
        if (portRepo.findByIdUsuario(input.getIdUsuario().longValue()).isPresent()) {
            throw new RuntimeException("El usuario ya tiene un portafolio");
        }
        
        var usuario = userRepo.findById(input.getIdUsuario().longValue())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var port = new Portafolio();
        port.setUsuario(usuario);
        port.setDescripcion(input.getDescripcion());
        port.setSkills(input.getSkills());
        port.setVisibilidad(
                input.getVisibilidad() != null ? input.getVisibilidad() : false
        );
        port.setUltimaActualizacion(LocalDateTime.now());

        // 🔥 REGISTRAR AUDITORÍA
        Integer auditId = auditoriaService.log(
                input.getIdUsuario(),                   // actor
                "CREAR_PORTAFOLIO",                     // acción
                "Creación de portafolio del usuario " + input.getIdUsuario()
        );

        port.setIdAuditoria(Long.valueOf(auditId));

        return portRepo.save(port);
    }


    @MutationMapping
    @Transactional
    public Portafolio actualizarPortafolio(@Argument GuardarPortafolioInput input) {

        var port = portRepo.findByIdUsuario(Long.valueOf(input.getIdUsuario()))
                .orElseThrow(() -> new RuntimeException("El usuario no tiene portafolio"));

        if (input.getDescripcion() != null) port.setDescripcion(input.getDescripcion());
        if (input.getSkills() != null) port.setSkills(input.getSkills());
        if (input.getVisibilidad() != null) port.setVisibilidad(input.getVisibilidad());

        port.setUltimaActualizacion(LocalDateTime.now());

        // 🔥 REGISTRAR AUDITORÍA
        Integer auditId = auditoriaService.log(
                input.getIdUsuario(),
                "ACTUALIZAR_PORTAFOLIO",
                "Actualización de portafolio del usuario " + input.getIdUsuario()
        );

        port.setIdAuditoria(Long.valueOf(auditId));

        return portRepo.save(port);
    }

}
