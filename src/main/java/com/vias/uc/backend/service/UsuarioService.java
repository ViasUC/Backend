package com.vias.uc.backend.service;

import org.springframework.stereotype.Service;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.model.dto.UsuarioInput;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.repository.PortafolioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PortafolioRepository portafolioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PortafolioRepository portafolioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.portafolioRepository = portafolioRepository;
    }

    // ============================================================
    //             ACTUALIZAR USUARIO + COMPLETITUD
    // ============================================================
    @Transactional
    public Usuario actualizarUsuario(Integer idUsuario, UsuarioInput input) {

        System.out.println("=== [DEBUG] Actualizando USUARIO con id=" + idUsuario + " ===");

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id_usuario: " + idUsuario));

        // --------------------------
        //        Actualizar datos
        // --------------------------
        if (input.nombre() != null) usuario.setNombre(input.nombre());
        if (input.apellido() != null) usuario.setApellido(input.apellido());
        if (input.email() != null && !input.email().isBlank()) usuario.setEmail(input.email());
        if (input.telefono() != null) usuario.setTelefono(input.telefono());
        if (input.ubicacion() != null) usuario.setUbicacion(input.ubicacion());

        // ======================================================
        //              ⚡ Recalcular COMPLETITUD ⚡
        // ======================================================
        recalcularCompletitud(usuario);

        Usuario actualizado = usuarioRepository.save(usuario);

        System.out.println("==> Usuario actualizado con completitud = " + actualizado.getCompletitud());

        return actualizado;
    }

    // ============================================================
    //             MÉTODO PRINCIPAL DE COMPLETITUD
    // ============================================================
    private void recalcularCompletitud(Usuario usuario) {
        int puntos = 0;

        // 1) Datos personales completos → 75%
        if (datosPersonalesCompletos(usuario)) {
            puntos = 75;
        }

        // 2) Portafolio completo → 100%
        if (portafolioCompleto(usuario.getIdUsuario())) {
            puntos = 100;
        }

        usuario.setCompletitud(puntos);

        System.out.println("==> Nueva COMPLETITUD calculada: " + puntos);
    }

    // ============================================================
    //             VALIDAR DATOS PERSONALES
    // ============================================================
    private boolean datosPersonalesCompletos(Usuario u) {
        return notEmpty(u.getNombre()) &&
               notEmpty(u.getApellido()) &&
               notEmpty(u.getEmail()) &&
               notEmpty(u.getTelefono()) &&
               notEmpty(u.getUbicacion());
    }

    private boolean notEmpty(String s) {
        return s != null && !s.isBlank();
    }

    // ============================================================
    //             VALIDAR PORTAFOLIO COMPLETO
    // ============================================================
    private boolean portafolioCompleto(Integer idUsuario) {
        return portafolioRepository.findByIdUsuario(idUsuario)
                .map(p -> notEmpty(p.getDescripcion()) &&
                          notEmpty(p.getSkills()))
                .orElse(false);
    }
}
