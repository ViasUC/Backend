package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Convenio;
import com.vias.uc.backend.model.Empresa;
import com.vias.uc.backend.model.dto.ConvenioInput;
import com.vias.uc.backend.model.dto.ConvenioOutput;
import com.vias.uc.backend.model.dto.ConvenioUpdateInput;
import com.vias.uc.backend.repository.ConvenioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConvenioService {

    private final ConvenioRepository convenioRepository;
    private final EmpresaService empresaService;
    private final AuditoriaService auditoriaService;

    public ConvenioService(ConvenioRepository convenioRepository, 
                          EmpresaService empresaService,
                          AuditoriaService auditoriaService) {
        this.convenioRepository = convenioRepository;
        this.empresaService = empresaService;
        this.auditoriaService = auditoriaService;
    }

    /**
     * UC-EMP-013: Solicitar Convenio
     * Crea una nueva solicitud de convenio
     */
    @Transactional
    public ConvenioOutput solicitarConvenio(ConvenioInput input, Long usuarioId) {
        // Validar que el usuario pertenece a una empresa
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        
        // Validaciones de campos obligatorios
        validarCamposObligatorios(input);
        
        // Crear el convenio
        Convenio convenio = new Convenio();
        convenio.setInstitucion(input.getInstitucion());
        convenio.setDescripcion(input.getDescripcion());
        convenio.setFechaIni(input.getFechaIni());
        convenio.setFechaFin(input.getFechaFin());
        convenio.setResponsables(input.getResponsables());
        convenio.setDuracion(input.getDuracion());
        convenio.setCantHoras(input.getCantHoras());
        convenio.setObjetivos(input.getObjetivos());
        convenio.setBeneficios(input.getBeneficios());
        convenio.setRequisitos(input.getRequisitos());
        convenio.setDocumentoAdjunto(input.getDocumentoAdjunto());
        convenio.setEmpresaId(empresa.getIdEmpresa());
        convenio.setIdUsuario(usuarioId.intValue());
        convenio.setEstado("Pendiente");
        convenio.setFechaCreacion(LocalDateTime.now());
        convenio.setFechaActualizacion(LocalDateTime.now());
        
        // Crear auditoría
        Integer idAuditoria = auditoriaService.log(
            usuarioId.intValue(),
            "CREAR_CONVENIO",
            "Solicitud de convenio con " + input.getInstitucion()
        );
        convenio.setIdAuditoria(idAuditoria);
        
        // Guardar convenio
        Convenio savedConvenio = convenioRepository.save(convenio);
        
        // TODO: Notificar al área administrativa (puede ser mediante eventos o servicio de notificaciones)
        
        return convertToOutput(savedConvenio, empresa);
    }

    /**
     * UC-EMP-017: Ver Estado de las Solicitudes
     * Lista todas las solicitudes de convenio de una empresa
     */
    public List<ConvenioOutput> listarSolicitudes(Long usuarioId) {
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        List<Convenio> solicitudes = convenioRepository.findSolicitudesByEmpresaId(empresa.getIdEmpresa());
        
        return solicitudes.stream()
                .map(c -> convertToOutput(c, empresa))
                .collect(Collectors.toList());
    }

    /**
     * UC-EMP-016: Ver Convenios Vigentes
     * Lista todos los convenios vigentes de una empresa
     */
    public List<ConvenioOutput> listarConveniosVigentes(Long usuarioId) {
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        List<Convenio> convenios = convenioRepository.findConveniosVigentesByEmpresaId(empresa.getIdEmpresa());
        
        return convenios.stream()
                .map(c -> convertToOutput(c, empresa))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el detalle de un convenio
     */
    public Optional<ConvenioOutput> obtenerDetalleConvenio(Integer idConven, Long usuarioId) {
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        Optional<Convenio> convenioOpt = convenioRepository.findByIdConvenAndEmpresaId(
            idConven, empresa.getIdEmpresa());
        
        return convenioOpt.map(c -> convertToOutput(c, empresa));
    }

    /**
     * UC-EMP-016: Actualizar Responsable de Convenio
     */
    @Transactional
    public ConvenioOutput actualizarResponsable(Integer idConven, ConvenioUpdateInput input, Long usuarioId) {
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        Optional<Convenio> convenioOpt = convenioRepository.findByIdConvenAndEmpresaId(
            idConven, empresa.getIdEmpresa());
        
        if (convenioOpt.isEmpty()) {
            throw new RuntimeException("Convenio no encontrado o no pertenece a la empresa");
        }
        
        Convenio convenio = convenioOpt.get();
        
        // Validar que el convenio está vigente
        if (!convenio.getEstado().equals("Vigente") && !convenio.getEstado().equals("Aprobado")) {
            throw new RuntimeException("Solo se pueden actualizar convenios vigentes");
        }
        
        // Actualizar responsables
        if (input.getResponsables() != null && !input.getResponsables().trim().isEmpty()) {
            convenio.setResponsables(input.getResponsables());
        }
        
        // Actualizar observaciones si se proporcionan
        if (input.getObservaciones() != null) {
            convenio.setObservaciones(input.getObservaciones());
        }
        
        convenio.setFechaActualizacion(LocalDateTime.now());
        
        Convenio updated = convenioRepository.save(convenio);
        return convertToOutput(updated, empresa);
    }

    /**
     * UC-EMP-016: Solicitar Renovación de Convenio
     */
    @Transactional
    public ConvenioOutput solicitarRenovacion(Integer idConven, Long usuarioId) {
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        Optional<Convenio> convenioOpt = convenioRepository.findByIdConvenAndEmpresaId(
            idConven, empresa.getIdEmpresa());
        
        if (convenioOpt.isEmpty()) {
            throw new RuntimeException("Convenio no encontrado o no pertenece a la empresa");
        }
        
        Convenio convenio = convenioOpt.get();
        
        // Validar que el convenio está vigente
        if (!convenio.getEstado().equals("Vigente") && !convenio.getEstado().equals("Aprobado")) {
            throw new RuntimeException("Solo se pueden renovar convenios vigentes");
        }
        
        // Cambiar estado a "En Renovación"
        convenio.setEstado("En Renovación");
        convenio.setObservaciones("Solicitud de renovación pendiente de aprobación");
        convenio.setFechaActualizacion(LocalDateTime.now());
        
        // TODO: Notificar al área administrativa sobre la solicitud de renovación
        
        Convenio updated = convenioRepository.save(convenio);
        return convertToOutput(updated, empresa);
    }

    /**
     * UC-EMP-016: Dar de Baja Convenio
     */
    @Transactional
    public ConvenioOutput darDeBajaConvenio(Integer idConven, String motivo, Long usuarioId) {
        Optional<Empresa> empresaOpt = empresaService.findByUsuarioId(usuarioId);
        if (empresaOpt.isEmpty()) {
            throw new RuntimeException("Usuario no pertenece a ninguna empresa");
        }
        
        Empresa empresa = empresaOpt.get();
        Optional<Convenio> convenioOpt = convenioRepository.findByIdConvenAndEmpresaId(
            idConven, empresa.getIdEmpresa());
        
        if (convenioOpt.isEmpty()) {
            throw new RuntimeException("Convenio no encontrado o no pertenece a la empresa");
        }
        
        Convenio convenio = convenioOpt.get();
        
        // Validar que el convenio está vigente
        if (!convenio.getEstado().equals("Vigente") && !convenio.getEstado().equals("Aprobado")) {
            throw new RuntimeException("Solo se pueden dar de baja convenios vigentes");
        }
        
        // Cambiar estado a Finalizado/Rescindido
        convenio.setEstado("Rescindido");
        convenio.setObservaciones(motivo != null ? motivo : "Convenio dado de baja por la empresa");
        convenio.setFechaActualizacion(LocalDateTime.now());
        
        // TODO: Notificar al área administrativa sobre la baja del convenio
        
        Convenio updated = convenioRepository.save(convenio);
        return convertToOutput(updated, empresa);
    }

    /**
     * Valida campos obligatorios de la solicitud
     */
    private void validarCamposObligatorios(ConvenioInput input) {
        if (input.getInstitucion() == null || input.getInstitucion().trim().isEmpty()) {
            throw new IllegalArgumentException("La institución es obligatoria");
        }
        if (input.getDescripcion() == null || input.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (input.getFechaIni() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        if (input.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de fin es obligatoria");
        }
        if (input.getFechaFin().isBefore(input.getFechaIni())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    /**
     * Convierte una entidad Convenio a ConvenioOutput
     */
    private ConvenioOutput convertToOutput(Convenio convenio, Empresa empresa) {
        ConvenioOutput output = new ConvenioOutput();
        output.setIdConven(convenio.getIdConven());
        output.setInstitucion(convenio.getInstitucion());
        output.setDescripcion(convenio.getDescripcion());
        output.setEstado(convenio.getEstado());
        output.setFechaIni(convenio.getFechaIni());
        output.setFechaFin(convenio.getFechaFin());
        output.setResponsables(convenio.getResponsables());
        output.setDuracion(convenio.getDuracion());
        output.setCantHoras(convenio.getCantHoras());
        output.setObjetivos(convenio.getObjetivos());
        output.setBeneficios(convenio.getBeneficios());
        output.setRequisitos(convenio.getRequisitos());
        output.setObservaciones(convenio.getObservaciones());
        output.setDocumentoAdjunto(convenio.getDocumentoAdjunto());
        output.setEmpresaId(convenio.getEmpresaId());
        output.setNombreEmpresa(empresa.getNombreEmpresa());
        output.setFechaCreacion(convenio.getFechaCreacion());
        output.setFechaActualizacion(convenio.getFechaActualizacion());
        return output;
    }
}
