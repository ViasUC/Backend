package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // En AlumnoService.java

    @Transactional
    public Alumno actualizarAlumno(Integer id, Alumno nuevosDatos) {

        // --- 1️⃣ a 4️⃣: (Tu código de búsqueda está perfecto) ---
        System.out.println("=== [DEBUG] 1. Recibido ID para buscar: " + id + " ===");
        Optional<Alumno> optionalAlumno = alumnoRepository.findByIdUsuario(id);
        if (optionalAlumno.isPresent()) {
            System.out.println("=== [DEBUG] 2. ¡ÉXITO! Alumno ENCONTRADO. ===");
        } else {
            System.out.println("=== [DEBUG] 2. ¡FALLO! Alumno NO ENCONTRADO. ===");
        }
        Alumno alumno = optionalAlumno.orElseThrow(() ->
                new RuntimeException("Alumno no encontrado con id_usuario: " + id));


        // --- 5️⃣: Verificar y actualizar datos del usuario ---
        Usuario usuario = alumno.getUsuario();
        Usuario nuevosDatosUsuario = nuevosDatos.getUsuario();

        if (nuevosDatosUsuario != null) {
            System.out.println("=== [DEBUG] 3. Actualizando datos del USUARIO asociado. ===");

            // ¡YA NO NECESITAS LOS IFs AQUÍ!
            // La consulta COALESCE se encarga de los nulls.

            // --- 7️⃣: Guardar cambios en usuario ---
            usuarioRepository.actualizarDatosUsuario(
                    usuario.getIdUsuario(),
                    nuevosDatosUsuario.getNombre(),
                    nuevosDatosUsuario.getApellido(),
                    nuevosDatosUsuario.getEmail()
            );
        } else {
            System.out.println("=== [DEBUG] 3. No se recibieron datos para actualizar el usuario. ===");
        }

        // --- 6️⃣: Verificar y actualizar datos del alumno (¡Ojo! Mismo problema) ---
        // APLICA LA MISMA LÓGICA DE COALESCE

        // Si no envías 'carrera', 'nuevosDatos.getCarrera()' será 'null' y borrará el dato.
        // Usamos la misma lógica: si el nuevo valor es null, usa el valor antiguo.

        alumno.setCarrera(
                COALESCE(nuevosDatos.getCarrera(), alumno.getCarrera())
        );
        alumno.setSemestre(
                COALESCE(nuevosDatos.getSemestre(), alumno.getSemestre())
        );

        System.out.println("=== [DEBUG] 4. Datos de Alumno actualizados. ===");


        // --- 7️⃣: Guardar cambios en alumno ---
        Alumno guardado = alumnoRepository.save(alumno);

        System.out.println("=== [DEBUG] 5. Datos guardados correctamente. ===");
        // ... (tus logs de salida) ...

        return guardado;
    }

    // --- 8️⃣: Añade esta función 'helper' a tu clase AlumnoService ---
// Esto es un reemplazo en Java para la función COALESCE de SQL.
    private <T> T COALESCE(T nuevoValor, T valorAntiguo) {
        return nuevoValor != null ? nuevoValor : valorAntiguo;
    }
}
