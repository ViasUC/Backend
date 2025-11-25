package com.vias.uc.backend.service;

import com.vias.uc.backend.model.*;
import com.vias.uc.backend.model.enums.RolEmpresa;
import com.vias.uc.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar usuarios asociados a empresas
 * Maneja solicitudes de acceso, aprobaciones, rechazos y cambios de rol
 */
@Service
@RequiredArgsConstructor
public class UsuarioEmpresaService {

    private final EmpresaUsuarioRepository empresaUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final AuditoriaRepository auditoriaRepository;

    /**
     * Solicita acceso de un usuario a una empresa con un rol específico
     * El usuario queda en estado pendiente (activo=false) hasta que un admin apruebe
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario que solicita acceso
     * @param rolSolicitado Rol que el usuario solicita tener en la empresa
     * @return La relación EmpresaUsuario creada en estado pendiente
     * @throws IllegalArgumentException si la empresa o usuario no existen, o si ya existe una relación
     */
    @Transactional
    public EmpresaUsuario solicitarAccesoEmpresa(Integer idEmpresa, Long idUsuario, RolEmpresa rolSolicitado) {
        // Validar que la empresa existe
        Empresa empresa = empresaRepository.findById(idEmpresa)
            .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Validar que no exista ya una relación (activa o pendiente)
        empresaUsuarioRepository.findByEmpresaAndUsuario(idEmpresa, idUsuario)
            .ifPresent(eu -> {
                if (eu.getActivo()) {
                    throw new IllegalArgumentException("El usuario ya tiene acceso a esta empresa");
                } else {
                    throw new IllegalArgumentException("Ya existe una solicitud pendiente para este usuario");
                }
            });
        
        // Crear auditoría para la solicitud
        Auditoria auditoria = Auditoria.builder()
            .actorId(idUsuario.intValue())
            .accion("SOLICITUD_ACCESO_EMPRESA")
            .detalle("Usuario " + usuario.getEmail() + " solicitó acceso a empresa " + empresa.getNombreEmpresa() + " con rol " + rolSolicitado.name())
            .fechaEvento(LocalDateTime.now())
            .build();
        auditoriaRepository.save(auditoria);
        
        // Crear relación en estado pendiente
        EmpresaUsuario empresaUsuario = new EmpresaUsuario();
        empresaUsuario.setEmpresa(idEmpresa);
        empresaUsuario.setUsuario(idUsuario);
        empresaUsuario.setEmpresaEntity(empresa);
        empresaUsuario.setUsuarioEntity(usuario);
        empresaUsuario.setRolEnEmpresa(rolSolicitado);
        empresaUsuario.setActivo(false); // Pendiente de aprobación
        empresaUsuario.setFechaAlta(LocalDateTime.now());
        empresaUsuario.setAuditoria(auditoria);
        
        return empresaUsuarioRepository.save(empresaUsuario);
    }

    /**
     * Lista todas las solicitudes pendientes de aprobación para una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @return Lista de relaciones EmpresaUsuario en estado pendiente
     */
    @Transactional(readOnly = true)
    public List<EmpresaUsuario> listarSolicitudesPendientes(Integer idEmpresa) {
        return empresaUsuarioRepository.findByEmpresaAndActivoFalse(idEmpresa);
    }

    /**
     * Aprueba la solicitud de un usuario para acceder a una empresa
     * Solo puede ser ejecutado por un administrador de la empresa
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario a aprobar
     * @param idAdministrador ID del usuario administrador que aprueba
     * @return La relación EmpresaUsuario actualizada (activo=true)
     * @throws IllegalArgumentException si no existe la solicitud o el admin no tiene permisos
     */
    @Transactional
    public EmpresaUsuario aprobarUsuario(Integer idEmpresa, Long idUsuario, Long idAdministrador) {
        // Verificar que quien aprueba es administrador
        if (!esAdministrador(idEmpresa, idAdministrador)) {
            throw new IllegalArgumentException("Solo los administradores pueden aprobar usuarios");
        }
        
        // Buscar la solicitud pendiente
        EmpresaUsuario empresaUsuario = empresaUsuarioRepository
            .findByEmpresaAndUsuario(idEmpresa, idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        if (empresaUsuario.getActivo()) {
            throw new IllegalArgumentException("El usuario ya está activo en la empresa");
        }
        
        // Aprobar
        empresaUsuario.setActivo(true);
        
        return empresaUsuarioRepository.save(empresaUsuario);
    }

    /**
     * Rechaza la solicitud de un usuario para acceder a una empresa
     * La relación es eliminada de la base de datos
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario a rechazar
     * @param idAdministrador ID del usuario administrador que rechaza
     * @throws IllegalArgumentException si no existe la solicitud o el admin no tiene permisos
     */
    @Transactional
    public void rechazarUsuario(Integer idEmpresa, Long idUsuario, Long idAdministrador) {
        // Verificar que quien rechaza es administrador
        if (!esAdministrador(idEmpresa, idAdministrador)) {
            throw new IllegalArgumentException("Solo los administradores pueden rechazar usuarios");
        }
        
        // Buscar la solicitud pendiente
        EmpresaUsuario empresaUsuario = empresaUsuarioRepository
            .findByEmpresaAndUsuario(idEmpresa, idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        if (empresaUsuario.getActivo()) {
            throw new IllegalArgumentException("No se puede rechazar un usuario ya activo");
        }
        
        // Eliminar la solicitud
        empresaUsuarioRepository.delete(empresaUsuario);
    }

    /**
     * Lista solo los usuarios activos de una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @return Lista de relaciones EmpresaUsuario con activo=true
     */
    @Transactional(readOnly = true)
    public List<EmpresaUsuario> listarUsuariosEmpresa(Integer idEmpresa) {
        return empresaUsuarioRepository.findByEmpresaAndActivoTrue(idEmpresa);
    }

    /**
     * Cambia el rol de un usuario en la empresa
     * Solo puede ser ejecutado por un administrador
     * No se puede cambiar el rol del único administrador
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario cuyo rol se va a cambiar
     * @param nuevoRol Nuevo rol a asignar
     * @param idAdministrador ID del usuario administrador que hace el cambio
     * @return La relación EmpresaUsuario actualizada
     * @throws IllegalArgumentException si no tiene permisos o intenta dejar la empresa sin admin
     */
    @Transactional
    public EmpresaUsuario cambiarRolUsuario(Integer idEmpresa, Long idUsuario, RolEmpresa nuevoRol, Long idAdministrador) {
        // Verificar que quien hace el cambio es administrador
        if (!esAdministrador(idEmpresa, idAdministrador)) {
            throw new IllegalArgumentException("Solo los administradores pueden cambiar roles");
        }
        
        // Buscar la relación
        EmpresaUsuario empresaUsuario = empresaUsuarioRepository
            .findByEmpresaAndUsuario(idEmpresa, idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en la empresa"));
        
        if (!empresaUsuario.getActivo()) {
            throw new IllegalArgumentException("No se puede cambiar el rol de un usuario pendiente");
        }
        
        // Si el usuario actual es ADMINISTRADOR y se quiere cambiar a otro rol,
        // verificar que no sea el único administrador
        if (empresaUsuario.getRolEnEmpresa() == RolEmpresa.ADMINISTRADOR 
            && nuevoRol != RolEmpresa.ADMINISTRADOR) {
            long cantidadAdmins = empresaUsuarioRepository.countAdministradoresActivos(idEmpresa);
            if (cantidadAdmins <= 1) {
                throw new IllegalArgumentException("No se puede cambiar el rol del único administrador");
            }
        }
        
        // Cambiar el rol
        empresaUsuario.setRolEnEmpresa(nuevoRol);
        
        return empresaUsuarioRepository.save(empresaUsuario);
    }

    /**
     * Desactiva un usuario de la empresa
     * Solo puede ser ejecutado por un administrador
     * No se puede desactivar al único administrador
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario a desactivar
     * @param idAdministrador ID del usuario administrador que desactiva
     * @throws IllegalArgumentException si no tiene permisos o intenta desactivar al único admin
     */
    @Transactional
    public void desactivarUsuario(Integer idEmpresa, Long idUsuario, Long idAdministrador) {
        // Verificar que quien desactiva es administrador
        if (!esAdministrador(idEmpresa, idAdministrador)) {
            throw new IllegalArgumentException("Solo los administradores pueden desactivar usuarios");
        }
        
        // Buscar la relación
        EmpresaUsuario empresaUsuario = empresaUsuarioRepository
            .findByEmpresaAndUsuario(idEmpresa, idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en la empresa"));
        
        // Si es administrador, verificar que no sea el único
        if (empresaUsuario.getRolEnEmpresa() == RolEmpresa.ADMINISTRADOR) {
            long cantidadAdmins = empresaUsuarioRepository.countAdministradoresActivos(idEmpresa);
            if (cantidadAdmins <= 1) {
                throw new IllegalArgumentException("No se puede desactivar al único administrador");
            }
        }
        
        // Desactivar
        empresaUsuario.setActivo(false);
        empresaUsuarioRepository.save(empresaUsuario);
    }

    /**
     * Verifica si un usuario es administrador de una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @return true si el usuario es administrador activo, false en caso contrario
     */
    public boolean esAdministrador(Integer idEmpresa, Long idUsuario) {
        return empresaUsuarioRepository.existsByEmpresaAndUsuarioAndRolEnEmpresaAndActivoTrue(
            idEmpresa, 
            idUsuario, 
            RolEmpresa.ADMINISTRADOR
        );
    }

    /**
     * Obtiene el rol de un usuario en una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @return El rol del usuario, o null si no tiene relación activa
     */
    public RolEmpresa obtenerRolUsuario(Integer idEmpresa, Long idUsuario) {
        Optional<EmpresaUsuario> resultado = empresaUsuarioRepository.findByEmpresaAndUsuario(idEmpresa, idUsuario);
        
        return resultado
            .filter(EmpresaUsuario::getActivo)
            .map(EmpresaUsuario::getRolEnEmpresa)
            .orElse(null);
    }

    /**
     * Verifica si un usuario pertenece a alguna empresa activa
     * 
     * @param idUsuario ID del usuario
     * @return true si el usuario tiene al menos una relación activa con alguna empresa
     */
    public boolean perteneceAEmpresa(Long idUsuario) {
        List<EmpresaUsuario> relaciones = empresaUsuarioRepository.findByUsuarioAndActivoTrue(idUsuario);
        return !relaciones.isEmpty();
    }
}
