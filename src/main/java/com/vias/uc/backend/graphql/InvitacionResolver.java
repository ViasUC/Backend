package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Convenio;
import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.EstadoOportunidad;
import com.vias.uc.backend.repository.ConvenioRepository;
import com.vias.uc.backend.repository.EmpresaUsuarioRepository;
import com.vias.uc.backend.repository.OportunidadRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class InvitacionResolver {

    private final AuditoriaService auditoriaService;
    private final OportunidadRepository oportunidadRepository;
    private final ConvenioRepository convenioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaUsuarioRepository empresaUsuarioRepository;

    /**
     * Query: verificarInvitacionExistente
     * Verifica si ya existe una invitacion previa para evitar duplicados
     */
    @QueryMapping
    public Map<String, Object> verificarInvitacionExistente(
            @Argument Integer idUsuario,
            @Argument Integer idOportunidad,
            @Argument Integer idConvenio
    ) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Auditoria> invitacionExistente = auditoriaService.verificarInvitacionExistente(
                idUsuario, 
                idOportunidad, 
                idConvenio
        );

        if (invitacionExistente.isPresent()) {
            Auditoria auditoria = invitacionExistente.get();
            result.put("existe", true);
            result.put("fechaEnvio", auditoria.getFechaEvento()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            result.put("mensaje", "Ya existe una invitacion previa para este candidato");
        } else {
            result.put("existe", false);
            result.put("fechaEnvio", null);
            result.put("mensaje", null);
        }

        return result;
    }

    /**
     * Query: obtenerOportunidadesActivas
     * Obtiene todas las oportunidades activas de una empresa
     */
    @QueryMapping
    public List<Oportunidad> obtenerOportunidadesActivas(@Argument Long idEmpresa) {
        return oportunidadRepository.findAll().stream()
                .filter(o -> o.getEstado() == EstadoOportunidad.activo)
                .filter(o -> {
                    if (o.getCreador() == null) return false;
                    Long creadorId = o.getCreador().getIdUsuario();
                    
                    // Buscar la relacion del usuario con la empresa
                    List<EmpresaUsuario> relaciones = empresaUsuarioRepository.findByUsuarioAndActivoTrue(creadorId);
                    
                    return relaciones.stream()
                            .anyMatch(rel -> rel.getEmpresa().equals(idEmpresa.intValue()));
                })
                .toList();
    }

    /**
     * Query: obtenerConveniosActivos
     * Obtiene solo los convenios con estado "Activo" de una empresa
     */
    @QueryMapping
    public List<Convenio> obtenerConveniosActivos(@Argument Long idEmpresa) {
        System.out.println("obtenerConveniosActivos - idEmpresa: " + idEmpresa);
        List<Convenio> convenios = convenioRepository.findByEmpresaId(idEmpresa.intValue()).stream()
                .filter(c -> "Activo".equalsIgnoreCase(c.getEstado()))
                .toList();
        System.out.println("Convenios activos encontrados: " + convenios.size());
        return convenios;
    }

    /**
     * Query: obtenerTodasSolicitudes
     * Obtiene todas las solicitudes de convenio de una empresa (incluye todos los estados)
     */
    @QueryMapping
    public List<Convenio> obtenerTodasSolicitudes(@Argument Long idEmpresa) {
        System.out.println("obtenerTodasSolicitudes - idEmpresa: " + idEmpresa);
        List<Convenio> convenios = convenioRepository.findByEmpresaId(idEmpresa.intValue());
        System.out.println("Total convenios encontrados: " + convenios.size());
        convenios.forEach(c -> System.out.println("Convenio: " + c.getIdConven() + " - Estado: " + c.getEstado()));
        return convenios;
    }

    /**
     * Mutation: registrarInvitacion
     * Registra una nueva invitacion en la tabla de auditoria
     */
    @MutationMapping
    public Map<String, Object> registrarInvitacion(@Argument Map<String, Object> input) {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer idUsuario = (Integer) input.get("idUsuario");
            Integer idEmpresa = (Integer) input.get("idEmpresa");
            Integer idOportunidad = (Integer) input.get("idOportunidad");
            Integer idConvenio = (Integer) input.get("idConvenio");
            String tipoInvitacion = (String) input.get("tipoInvitacion");
            String canal = (String) input.get("canal");
            String mensaje = (String) input.get("mensaje");
            Integer actorId = (Integer) input.get("actorId");

            // Validar que el usuario existe
            Usuario usuario = usuarioRepository.findById(idUsuario.longValue())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Validar canal de envio
            if ("EMAIL".equals(canal) && (usuario.getEmail() == null || usuario.getEmail().isEmpty())) {
                result.put("success", false);
                result.put("message", "El usuario no tiene email registrado");
                result.put("idAuditoria", null);
                return result;
            }

            if ("WHATSAPP".equals(canal) && (usuario.getTelefono() == null || usuario.getTelefono().isEmpty())) {
                result.put("success", false);
                result.put("message", "El usuario no tiene telefono registrado");
                result.put("idAuditoria", null);
                return result;
            }

            // Verificar si ya existe una invitacion previa
            Optional<Auditoria> invitacionExistente = auditoriaService.verificarInvitacionExistente(
                    idUsuario, 
                    idOportunidad, 
                    idConvenio
            );

            if (invitacionExistente.isPresent()) {
                Auditoria auditoria = invitacionExistente.get();
                String fechaEnvio = auditoria.getFechaEvento()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                
                result.put("success", false);
                result.put("message", "Ya se envio una invitacion a este candidato el " + fechaEnvio);
                result.put("idAuditoria", null);
                return result;
            }

            // Validar que la oportunidad existe y esta activa
            if (idOportunidad != null) {
                Oportunidad oportunidad = oportunidadRepository.findById(idOportunidad)
                        .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada"));
                
                if (oportunidad.getEstado() != EstadoOportunidad.activo) {
                    result.put("success", false);
                    result.put("message", "La oportunidad no esta activa");
                    result.put("idAuditoria", null);
                    return result;
                }
            }

            // Registrar la invitacion en auditoria
            Auditoria nuevaInvitacion = auditoriaService.registrarInvitacion(
                    idUsuario,
                    idEmpresa,
                    idOportunidad,
                    idConvenio,
                    tipoInvitacion,
                    canal,
                    mensaje,
                    actorId
            );

            result.put("success", true);
            result.put("message", "Invitacion registrada exitosamente");
            result.put("idAuditoria", nuevaInvitacion.getIdAuditoria());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error al registrar invitacion: " + e.getMessage());
            result.put("idAuditoria", null);
        }

        return result;
    }

    /**
     * Mutation: darDeBajaConvenio
     * Cambia el estado del convenio a Finalizado
     */
    @MutationMapping
    public Boolean darDeBajaConvenio(@Argument Integer idConvenio, @Argument String motivo) {
        try {
            System.out.println(">>> Dar de baja convenio: idConvenio=" + idConvenio + ", motivo=" + motivo);
            
            Convenio convenio = convenioRepository.findById(idConvenio)
                    .orElseThrow(() -> new RuntimeException("Convenio no encontrado: " + idConvenio));
            
            System.out.println(">>> Convenio encontrado: " + convenio.getInstitucion() + ", estado actual=" + convenio.getEstado());
            
            // Cambiar estado a Finalizado
            convenio.setEstado("Finalizado");
            
            // Guardar observaciones con el motivo
            String observacionesActuales = convenio.getObservaciones() != null ? convenio.getObservaciones() : "";
            String nuevasObservaciones = observacionesActuales + 
                    (observacionesActuales.isEmpty() ? "" : "\n") +
                    "[Baja] " + motivo;
            convenio.setObservaciones(nuevasObservaciones);
            
            convenioRepository.save(convenio);
            System.out.println(">>> Convenio dado de baja exitosamente");
            
            return true;
        } catch (Exception e) {
            System.err.println(">>> Error al dar de baja convenio: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al dar de baja el convenio: " + e.getMessage());
        }
    }

    /**
     * Mutation: actualizarConvenio
     * Actualiza los datos de un convenio existente
     */
    @MutationMapping
    public Convenio actualizarConvenio(
            @Argument Integer idConvenio,
            @Argument String institucion,
            @Argument String descripcion,
            @Argument String fechaIni,
            @Argument String fechaFin,
            @Argument String responsables,
            @Argument String duracion,
            @Argument String cantHoras,
            @Argument String objetivos,
            @Argument String beneficios) {
        
        System.out.println("actualizarConvenio - idConvenio: " + idConvenio);
        
        try {
            Convenio convenio = convenioRepository.findById(idConvenio)
                    .orElseThrow(() -> new RuntimeException("Convenio no encontrado: " + idConvenio));
            
            // Actualizar campos
            convenio.setInstitucion(institucion);
            convenio.setDescripcion(descripcion);
            
            // Convertir strings a LocalDate
            if (fechaIni != null && !fechaIni.isEmpty()) {
                convenio.setFechaIni(java.time.LocalDate.parse(fechaIni));
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                convenio.setFechaFin(java.time.LocalDate.parse(fechaFin));
            }
            
            convenio.setResponsables(responsables);
            convenio.setDuracion(duracion);
            convenio.setCantHoras(cantHoras);
            convenio.setObjetivos(objetivos);
            convenio.setBeneficios(beneficios);
            
            Convenio convenioActualizado = convenioRepository.save(convenio);
            System.out.println("Convenio actualizado exitosamente: " + convenioActualizado.getIdConven());
            
            return convenioActualizado;
        } catch (Exception e) {
            System.err.println("Error al actualizar convenio: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el convenio: " + e.getMessage());
        }
    }

    /**
     * Mutation: toggleActivoConvenio
     * Activa o desactiva un convenio cambiando su estado
     * Activo=true -> estado "Activo", Activo=false -> estado "Aprobado"
     */
    @MutationMapping
    public Boolean toggleActivoConvenio(@Argument Integer idConvenio, @Argument Boolean activo) {
        System.out.println("toggleActivoConvenio - idConvenio: " + idConvenio + ", activo: " + activo);
        
        try {
            Convenio convenio = convenioRepository.findById(idConvenio)
                    .orElseThrow(() -> new RuntimeException("Convenio no encontrado: " + idConvenio));
            
            // Cambiar el estado según el parámetro activo
            String nuevoEstado = activo ? "Activo" : "Aprobado";
            convenio.setEstado(nuevoEstado);
            convenioRepository.save(convenio);
            
            System.out.println("Convenio toggle exitoso: " + idConvenio + " -> estado=" + nuevoEstado);
            return true;
        } catch (Exception e) {
            System.err.println("Error al toggle convenio: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al cambiar estado del convenio: " + e.getMessage());
        }
    }

    /**
     * Mutation: aprobarConvenio
     * Aprueba un convenio cambiando su estado de Pendiente a Aprobado
     */
    @MutationMapping
    public Boolean aprobarConvenio(@Argument Integer idConvenio) {
        System.out.println("aprobarConvenio - idConvenio: " + idConvenio);
        
        try {
            Convenio convenio = convenioRepository.findById(idConvenio)
                    .orElseThrow(() -> new RuntimeException("Convenio no encontrado: " + idConvenio));
            
            if (!"Pendiente".equalsIgnoreCase(convenio.getEstado())) {
                throw new RuntimeException("Solo se pueden aprobar convenios en estado Pendiente. Estado actual: " + convenio.getEstado());
            }
            
            convenio.setEstado("Aprobado");
            convenioRepository.save(convenio);
            
            System.out.println("Convenio aprobado exitosamente: " + idConvenio);
            return true;
        } catch (Exception e) {
            System.err.println("Error al aprobar convenio: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al aprobar el convenio: " + e.getMessage());
        }
    }
}
