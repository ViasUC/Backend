package com.vias.uc.backend.graphql.query;

// --- IMPORTACIONES NECESARIAS ---
import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.dto.AlumnoPerfilOutput;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.service.AlumnoService; // 1. Importar el SERVICIO

// 2. IMPORTAR TU CLASE 'INPUT' (¡VERIFICA ESTE PAQUETE!)
// Asegúrate de que este 'import' apunte a donde sea que tengas tu 'AlumnoInput.java'
import com.vias.uc.backend.model.dto.AlumnoInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping; // 3. Importar la anotación de Mutación
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AlumnoResolver {

    private final AlumnoService alumnoService;

    public AlumnoResolver(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    // ===== Query =====
    @QueryMapping
    public List<Alumno> alumnos() {
        return alumnoService.listarAlumnos();
    }

    // Tenés ambas en el schema: usuario decide si deja una o las dos
    @QueryMapping
    public Alumno alumno(@Argument Long id) {
        return alumnoService.obtenerAlumnoPorId(id).orElse(null);
    }

    @QueryMapping
    public Alumno alumnoPorId(@Argument Long id) {
        return alumnoService.obtenerAlumnoPorId(id).orElse(null);
    }

    // ===== Mutation =====
    @MutationMapping
    public Alumno registrarAlumno(@Argument RegistroAlumnoInput input) {
        return alumnoService.registrarAlumno(input);
    }

    @MutationMapping
    public Alumno actualizarAlumno(@Argument Long id, @Argument AlumnoInput input) {
        return alumnoService.actualizarDatos(id, input);
    }

    @QueryMapping
    public AlumnoPerfilOutput consultarPerfil(@Argument Long idUsuario) {
        return alumnoService.consultarPerfil(idUsuario);
    }
}
