package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Administrador;
import com.vias.uc.backend.model.Egresado;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AdministradorRepository;
import com.vias.uc.backend.repository.EgresadoRepository;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioFieldResolvers {

    private final AdministradorRepository adminRepo;
    private final EgresadoRepository egresadoRepo;

    public UsuarioFieldResolvers(
            AdministradorRepository adminRepo,
            EgresadoRepository egresadoRepo
    ) {
        this.adminRepo = adminRepo;
        this.egresadoRepo = egresadoRepo;
    }

    @SchemaMapping(typeName = "Usuario", field = "adminData")
    public Administrador getAdminData(Usuario usuario) {
        // como idUsuario ES el ID de Admin en la tabla, se usa findById
        return adminRepo.findById(usuario.getIdUsuario()).orElse(null);
    }

    @SchemaMapping(typeName = "Usuario", field = "egresadoData")
    public Egresado getEgresadoData(Usuario usuario) {
        // igual aquí: la PK es idUsuario en la tabla egresado
        return egresadoRepo.findById(usuario.getIdUsuario()).orElse(null);
    }
}
