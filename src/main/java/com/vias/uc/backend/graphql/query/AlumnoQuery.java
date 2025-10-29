package com.vias.uc.backend.graphql.query;

// --- IMPORTACIONES NECESARIAS ---
import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AlumnoRepository;
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
public class AlumnoQuery {

    private final AlumnoRepository alumnoRepository;
    private final AlumnoService alumnoService; // 4. Declarar el SERVICIO

    // 5. Modificar el constructor para INYECTAR AMBOS
    public AlumnoQuery(AlumnoRepository alumnoRepository, AlumnoService alumnoService) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoService = alumnoService; // Inyectar el servicio
    }

    // --- TU MÉTODO DE CONSULTA (YA FUNCIONABA) ---
    @QueryMapping
    public List<Alumno> alumnos() {
        return alumnoRepository.findAll();
    }

    // --- 6. MÉTODO DE MUTACIÓN (ESTO ES LO QUE FALTABA) ---
    @MutationMapping
    public Alumno actualizarAlumno(@Argument Integer id, @Argument AlumnoInput input) {

        System.out.println("=== [DEBUG] 0. ¡Mutación RECIBIDA en AlumnoQuery! ID: " + id + " ===");

        // --- 6. CONVERTIR EL 'INPUT' A UNA ENTIDAD 'ALUMNO' ---

        Usuario usuarioDatosNuevos = new Usuario();

        // 👇 CORRECCIÓN AQUÍ: usa .usuario() en lugar de .getUsuario()
        if (input.usuario() != null) {
            usuarioDatosNuevos.setNombre(input.usuario().nombre());
            usuarioDatosNuevos.setApellido(input.usuario().apellido());
            usuarioDatosNuevos.setEmail(input.usuario().email());
        }

        Alumno alumnoDatosNuevos = new Alumno();
        alumnoDatosNuevos.setUsuario(usuarioDatosNuevos);

        // 👇 CORRECCIÓN AQUÍ: usa .carrera() y .semestre()
        alumnoDatosNuevos.setCarrera(input.carrera());
        alumnoDatosNuevos.setSemestre(input.semestre());

        // --- 9. LLAMAR AL SERVICIO ---
        return alumnoService.actualizarAlumno(id, alumnoDatosNuevos);
    }
}