package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Sesion;
import com.vias.uc.backend.repository.SesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionService {
    
    private final SesionRepository sesionRepository;
    private final AuditoriaService auditoriaService;
    
    /**
     * Registra un inicio de sesión exitoso
     */
    @Transactional
    public Sesion registrarLoginExitoso(Integer idUsuario, String email) {
        // Crear registro en auditoría
        Integer idAuditoria = auditoriaService.log(
            idUsuario, 
            "LOGIN_EXITOSO", 
            "Usuario " + email + " inició sesión exitosamente"
        );
        
        // Crear sesión
        Sesion sesion = Sesion.builder()
            .idUsuario(idUsuario)
            .fechaIni(LocalDateTime.now())
            .fechaFin(null)  // null indica que está activa
            .idAuditoria(idAuditoria)
            .build();
        
        return sesionRepository.save(sesion);
    }
    
    /**
     * Registra el cierre de sesión (logout)
     */
    @Transactional
    public void registrarLogout(Integer idUsuario, String email) {
        // Buscar la sesión activa más reciente
        sesionRepository.findFirstByIdUsuarioAndFechaFinIsNullOrderByFechaIniDesc(idUsuario)
            .ifPresent(sesion -> {
                sesion.setFechaFin(LocalDateTime.now());
                sesionRepository.save(sesion);
                
                // Registrar en auditoría
                auditoriaService.log(
                    idUsuario,
                    "LOGOUT",
                    "Usuario " + email + " cerró sesión"
                );
            });
    }
    
    /**
     * Cierra todas las sesiones activas de un usuario
     */
    @Transactional
    public void cerrarTodasLasSesiones(Integer idUsuario) {
        List<Sesion> sesionesActivas = sesionRepository.findByIdUsuarioAndFechaFinIsNull(idUsuario);
        LocalDateTime ahora = LocalDateTime.now();
        
        sesionesActivas.forEach(sesion -> {
            sesion.setFechaFin(ahora);
            sesionRepository.save(sesion);
        });
    }
    
    /**
     * Verifica si un usuario tiene sesiones activas
     */
    public boolean tieneSesionActiva(Integer idUsuario) {
        return !sesionRepository.findByIdUsuarioAndFechaFinIsNull(idUsuario).isEmpty();
    }

    /**
     * Obtiene el ID de usuario desde un token
     * NOTA: Esta es una implementación simplificada.
     * En un sistema real, se debería validar y decodificar un JWT
     * 
     * @param token Token de sesión
     * @return ID del usuario
     */
    public Long getUserIdFromToken(String token) {
        // TODO: Implementar validación real de JWT
        // Por ahora, retornamos un valor basado en la sesión más reciente
        // En un sistema real, se debería decodificar el JWT y extraer el userId
        
        // Implementación temporal: asumimos que el token es válido
        // y retornamos un ID de usuario mock
        try {
            // Aquí debería ir la lógica de decodificación JWT
            // Por ahora, lanzamos una excepción para forzar autenticación real
            throw new RuntimeException("Implementar validación de JWT");
        } catch (Exception e) {
            return null;
        }
    }
}
