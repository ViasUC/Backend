package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Empresa;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.EmpresaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmpresaResolver {

    private final EmpresaService empresaService;
    private final UsuarioRepository usuarioRepository;

    public EmpresaResolver(EmpresaService empresaService, UsuarioRepository usuarioRepository) {
        this.empresaService = empresaService;
        this.usuarioRepository = usuarioRepository;
    }

    @QueryMapping
    public Map<String, Object> miEmpresa(@Argument Long idUsuario) {
        System.out.println(">>> Query miEmpresa recibido");
        System.out.println(">>> idUsuario: " + idUsuario);
        
        if (idUsuario == null) {
            System.out.println(">>> ERROR: idUsuario es null");
            throw new RuntimeException("Se requiere idUsuario");
        }
        
        System.out.println(">>> Buscando empresa por idUsuario en tabla empresa_usuario: " + idUsuario);
        Empresa empresa = empresaService.findByUsuarioId(idUsuario).orElse(null);
        
        if (empresa == null) {
            System.out.println(">>> ERROR: Empresa no encontrada para idUsuario: " + idUsuario);
            throw new RuntimeException("Empresa no encontrada para el usuario");
        }
        
        System.out.println(">>> Empresa encontrada: " + empresa.getNombreEmpresa());
        
        return convertEmpresaToMap(empresa);
    }

    @MutationMapping
    public Map<String, Object> actualizarEmpresa(@Argument("input") Map<String, Object> input, 
                                                   @Argument Long idUsuario) {
        System.out.println(">>> Mutation actualizarEmpresa recibido");
        System.out.println(">>> Input: " + input);
        System.out.println(">>> idUsuario: " + idUsuario);
        
        if (idUsuario == null) {
            System.out.println(">>> ERROR: idUsuario es null");
            throw new RuntimeException("Se requiere idUsuario");
        }
        
        System.out.println(">>> Buscando empresa por idUsuario en tabla empresa_usuario: " + idUsuario);
        Empresa empresa = empresaService.findByUsuarioId(idUsuario).orElse(null);
        
        if (empresa == null) {
            System.out.println(">>> ERROR: Empresa no encontrada para idUsuario: " + idUsuario);
            throw new RuntimeException("Empresa no encontrada");
        }
        
        // Actualizar campos
        if (input.containsKey("nombreEmpresa")) {
            empresa.setNombreEmpresa((String) input.get("nombreEmpresa"));
        }
        if (input.containsKey("ruc")) {
            empresa.setRuc((String) input.get("ruc"));
        }
        if (input.containsKey("razonSocial")) {
            empresa.setRazonSocial((String) input.get("razonSocial"));
        }
        if (input.containsKey("contacto")) {
            empresa.setContacto((String) input.get("contacto"));
        }
        if (input.containsKey("ubicacion")) {
            empresa.setUbicacion((String) input.get("ubicacion"));
        }
        if (input.containsKey("email")) {
            empresa.setEmail((String) input.get("email"));
        }
        if (input.containsKey("descripcion")) {
            empresa.setDescripcion((String) input.get("descripcion"));
        }
        
        Empresa actualizada = empresaService.updateEmpresa(empresa);
        
        System.out.println(">>> Empresa actualizada: " + actualizada.getNombreEmpresa());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Empresa actualizada correctamente");
        response.put("empresa", convertEmpresaToMap(actualizada));
        
        return response;
    }

    private Map<String, Object> convertEmpresaToMap(Empresa empresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", empresa.getIdEmpresa());
        map.put("nombreEmpresa", empresa.getNombreEmpresa());
        map.put("ruc", empresa.getRuc());
        map.put("razonSocial", empresa.getRazonSocial());
        map.put("contacto", empresa.getContacto());
        map.put("ubicacion", empresa.getUbicacion());
        map.put("email", empresa.getEmail());
        map.put("descripcion", empresa.getDescripcion());
        return map;
    }
}
