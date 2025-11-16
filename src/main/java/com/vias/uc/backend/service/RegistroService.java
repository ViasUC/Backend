package com.vias.uc.backend.service;

import com.vias.uc.backend.model.*;
import com.vias.uc.backend.model.enums.RolUsuario;
import com.vias.uc.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaUsuarioRepository empresaUsuarioRepository;
    private final AuditoriaService auditoriaService;
    private final SesionService sesionService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Usuario registrarUsuario(RegisterInput input) {
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear auditoría para el registro
        Auditoria auditoria = auditoriaService.crear(
            "REGISTRO_USUARIO",
            "Nuevo usuario registrado: " + input.getEmail(),
            null
        );

        // Crear usuario base
        Usuario usuario = new Usuario();
        usuario.setNombre(input.getNombre());
        usuario.setApellido(input.getApellido());
        usuario.setEmail(input.getEmail());
        usuario.setTelefono(input.getTelefono());
        usuario.setPassword(passwordEncoder.encode(input.getPassword()));
        usuario.setUbicacion(input.getUbicacion() != null ? input.getUbicacion() : "Asunción");
        usuario.setCompletitud(50); // Completitud inicial
        usuario.setAuditoria(auditoria);

        // Asignar rol según tipo de usuario
        RolUsuario rol = mapearTipoUsuarioARol(input.getTipoUsuario());
        usuario.setRolPrincipal(rol);

        // Guardar usuario
        usuario = usuarioRepository.save(usuario);

        // Si es empleador, crear empresa
        if (rol == RolUsuario.empresa && input.getNombreEmpresa() != null) {
            crearEmpresa(input, usuario);
        }

        // Registrar sesión inicial
        sesionService.registrarLoginExitoso(usuario.getIdUsuario().intValue(), usuario.getEmail());

        return usuario;
    }

    private void crearEmpresa(RegisterInput input, Usuario usuario) {
        // Crear auditoría para la empresa
        Auditoria auditoriaEmpresa = auditoriaService.crear(
            "REGISTRO_EMPRESA",
            "Nueva empresa registrada: " + input.getNombreEmpresa(),
            usuario.getIdUsuario().intValue()
        );

        // Crear empresa
        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa(input.getNombreEmpresa());
        empresa.setRuc(input.getRuc());
        empresa.setRazonSocial(input.getRazonSocial());
        empresa.setContacto(input.getContacto());
        empresa.setUbicacion(input.getUbicacionEmpresa() != null ? input.getUbicacionEmpresa() : "Asunción");
        empresa.setEmail(input.getEmailEmpresa());
        empresa.setDescripcion("Empresa registrada a través de VIAS-UC");
        empresa.setIdAuditoria(Math.toIntExact(auditoriaEmpresa.getIdAuditoria()));

        empresa = empresaRepository.save(empresa);

        // Crear auditoría para la relación empresa-usuario
        Auditoria auditoriaRelacion = auditoriaService.crear(
            "VINCULACION_EMPRESA_USUARIO",
            "Usuario " + usuario.getEmail() + " vinculado a empresa " + empresa.getNombreEmpresa(),
            usuario.getIdUsuario().intValue()
        );

        // Crear relación en empresa_usuario
        EmpresaUsuario empresaUsuario = new EmpresaUsuario();
        empresaUsuario.setEmpresa(empresa.getIdEmpresa());
        empresaUsuario.setUsuario(usuario.getIdUsuario());
        empresaUsuario.setEmpresaEntity(empresa);
        empresaUsuario.setUsuarioEntity(usuario);
        empresaUsuario.setRolEnEmpresa(input.getRolEnEmpresa() != null ? input.getRolEnEmpresa() : "Propietario");
        empresaUsuario.setActivo(true);
        empresaUsuario.setAuditoria(auditoriaRelacion);

        empresaUsuarioRepository.save(empresaUsuario);
    }

    private RolUsuario mapearTipoUsuarioARol(String tipoUsuario) {
        return switch (tipoUsuario.toUpperCase()) {
            case "EMPLEADOR" -> RolUsuario.empresa;
            case "ESTUDIANTE" -> RolUsuario.alumno;
            case "EGRESADO" -> RolUsuario.egresado;
            case "DOCENTE" -> RolUsuario.profesor;
            case "ADMIN" -> RolUsuario.administrador;
            default -> RolUsuario.alumno;
        };
    }

    // Clase interna para el input
    public static class RegisterInput {
        private String tipoUsuario;
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
        private String password;
        private String ubicacion;
        // Datos empresa
        private String nombreEmpresa;
        private String ruc;
        private String razonSocial;
        private String contacto;
        private String ubicacionEmpresa;
        private String emailEmpresa;
        private String rolEnEmpresa;

        // Getters y Setters
        public String getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getUbicacion() { return ubicacion; }
        public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
        
        public String getNombreEmpresa() { return nombreEmpresa; }
        public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
        
        public String getRuc() { return ruc; }
        public void setRuc(String ruc) { this.ruc = ruc; }
        
        public String getRazonSocial() { return razonSocial; }
        public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
        
        public String getContacto() { return contacto; }
        public void setContacto(String contacto) { this.contacto = contacto; }
        
        public String getUbicacionEmpresa() { return ubicacionEmpresa; }
        public void setUbicacionEmpresa(String ubicacionEmpresa) { this.ubicacionEmpresa = ubicacionEmpresa; }
        
        public String getEmailEmpresa() { return emailEmpresa; }
        public void setEmailEmpresa(String emailEmpresa) { this.emailEmpresa = emailEmpresa; }
        
        public String getRolEnEmpresa() { return rolEnEmpresa; }
        public void setRolEnEmpresa(String rolEnEmpresa) { this.rolEnEmpresa = rolEnEmpresa; }
    }
}
