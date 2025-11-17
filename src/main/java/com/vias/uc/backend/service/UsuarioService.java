package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Investigador;
import com.vias.uc.backend.model.Profesor;

public interface UsuarioService {
    boolean exists(Integer id);
    String rol(Integer id);
    boolean activo(Integer id);

    // Altas
    Profesor registrarProfesor(Integer idActor, ProfesorInput input);
    Investigador registrarInvestigador(Integer idActor, InvestigadorInput input);


    // Actualizaciones
    Profesor actualizarProfesor(Integer id, UsuarioService.ProfesorInput input);
    Investigador actualizarInvestigador(Integer id, UsuarioService.InvestigadorInput input);


    // ===== DTOs anidados (públicos) =====
    class UsuarioInput {
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
        private String ubicacion;
        private String password;

        public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
        public String getApellido(){return apellido;} public void setApellido(String v){apellido=v;}
        public String getEmail(){return email;} public void setEmail(String v){email=v;}
        public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
        public String getUbicacion(){return ubicacion;} public void setUbicacion(String v){ubicacion=v;}
        public String getPassword(){return password;} public void setPassword(String v){password=v;}
    }

    class ProfesorInput {
        private UsuarioInput usuario;
        private String departamento;
        private String categoriaDocente;
        private String areasDocentes;

        public UsuarioInput getUsuario(){return usuario;} public void setUsuario(UsuarioInput v){usuario=v;}
        public String getDepartamento(){return departamento;} public void setDepartamento(String v){departamento=v;}
        public String getCategoriaDocente(){return categoriaDocente;} public void setCategoriaDocente(String v){categoriaDocente=v;}
        public String getAreasDocentes(){return areasDocentes;} public void setAreasDocentes(String v){areasDocentes=v;}
    }

    class InvestigadorInput {
        private UsuarioInput usuario;
        private String areasInvestigacion;
        private String afiliaciones;
        private Integer hindex;

        public UsuarioInput getUsuario(){return usuario;} public void setUsuario(UsuarioInput v){usuario=v;}
        public String getAreasInvestigacion(){return areasInvestigacion;} public void setAreasInvestigacion(String v){areasInvestigacion=v;}
        public String getAfiliaciones(){return afiliaciones;} public void setAfiliaciones(String v){afiliaciones=v;}
        public Integer getHindex(){return hindex;} public void setHindex(Integer v){hindex=v;}
    }
}
