package com.vias.uc.backend.service;

import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PortafolioService {

    private final EntityManager entityManager;
    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Busca portafolios con filtros opcionales
     */
    public Map<String, Object> buscarPortafolios(Map<String, Object> filtros) {
        System.out.println(">>> Búsqueda de portafolios con filtros: " + filtros);
        
        // Paginación
        int pagina = filtros.containsKey("pagina") ? ((Number) filtros.get("pagina")).intValue() : 1;
        int limite = filtros.containsKey("limite") ? ((Number) filtros.get("limite")).intValue() : 20;
        int offset = (pagina - 1) * limite;
        
        // Filtros opcionales - normalizar valores vacíos a null
        String textoBusqueda = filtros.get("textoBusqueda") != null && !filtros.get("textoBusqueda").toString().trim().isEmpty() 
                               ? filtros.get("textoBusqueda").toString() : null;
        String carrera = filtros.get("carrera") != null && !filtros.get("carrera").toString().trim().isEmpty() 
                         ? filtros.get("carrera").toString() : null;
        String ubicacion = filtros.get("ubicacion") != null && !filtros.get("ubicacion").toString().trim().isEmpty() 
                           ? filtros.get("ubicacion").toString() : null;
        
        System.out.println(">>> Filtros - texto: " + textoBusqueda + ", carrera: " + carrera + ", ubicacion: " + ubicacion);
        
        // Construir consulta SQL dinámica
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id_portafolio, p.descripcion, p.skills, ");
        sql.append("u.id_usuario, u.nombre, u.apellido, u.ubicacion, a.carrera, u.email, u.telefono ");
        sql.append("FROM portafolio p ");
        sql.append("INNER JOIN usuarios u ON p.id_usuario = u.id_usuario ");
        sql.append("LEFT JOIN alumnos a ON u.id_usuario = a.id_usuario ");
        sql.append("WHERE p.visibilidad = true ");
        
        // Filtro de búsqueda global (nombre, apellido o habilidades)
        if (textoBusqueda != null) {
            sql.append("AND (");
            sql.append("LOWER(u.nombre) LIKE ? OR ");
            sql.append("LOWER(u.apellido) LIKE ? OR ");
            sql.append("LOWER(p.skills) LIKE ? ");
            sql.append(") ");
        }
        
        // Filtros específicos
        if (carrera != null) {
            sql.append("AND a.carrera IS NOT NULL AND LOWER(a.carrera) LIKE ? ");
        }
        if (ubicacion != null) {
            sql.append("AND u.ubicacion IS NOT NULL AND LOWER(u.ubicacion) LIKE ? ");
        }
        
        sql.append("ORDER BY u.nombre, u.apellido ");
        sql.append("LIMIT ? OFFSET ?");
        
        System.out.println(">>> SQL: " + sql);
        
        // Ejecutar consulta
        Query query = entityManager.createNativeQuery(sql.toString());
        
        // Setear parámetros
        int paramIndex = 1;
        if (textoBusqueda != null) {
            String textoPattern = "%" + textoBusqueda.toLowerCase() + "%";
            query.setParameter(paramIndex++, textoPattern);
            query.setParameter(paramIndex++, textoPattern);
            query.setParameter(paramIndex++, textoPattern);
        }
        if (carrera != null) {
            query.setParameter(paramIndex++, "%" + carrera.toLowerCase() + "%");
        }
        if (ubicacion != null) {
            query.setParameter(paramIndex++, "%" + ubicacion.toLowerCase() + "%");
        }
        query.setParameter(paramIndex++, limite);
        query.setParameter(paramIndex, offset);
        
        @SuppressWarnings("unchecked")
        List<Object[]> resultadosRaw = query.getResultList();
        
        // Convertir resultados a mapas
        List<Map<String, Object>> portafolios = new ArrayList<>();
        for (Object[] row : resultadosRaw) {
            Map<String, Object> portafolio = new HashMap<>();
            portafolio.put("idPortafolio", row[0]);
            portafolio.put("descripcion", row[1]);
            portafolio.put("skills", row[2]);
            portafolio.put("usuarioId", row[3]);
            portafolio.put("nombre", row[4]);
            portafolio.put("apellido", row[5]);
            portafolio.put("ubicacion", row[6]);
            portafolio.put("carrera", row[7]);
            portafolio.put("email", row[8]);
            portafolio.put("telefono", row[9]);
            portafolios.add(portafolio);
        }
        
        // Contar total de resultados
        int total = contarResultados(filtros);
        int totalPaginas = (int) Math.ceil((double) total / limite);
        
        // Preparar respuesta
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("portafolios", portafolios);
        resultado.put("total", total);
        resultado.put("pagina", pagina);
        resultado.put("totalPaginas", totalPaginas);
        
        System.out.println(">>> Encontrados: " + portafolios.size() + " de " + total + " total");
        
        return resultado;
    }
    
    /**
     * Cuenta el total de resultados sin paginación
     */
    private int contarResultados(Map<String, Object> filtros) {
        String textoBusqueda = filtros.get("textoBusqueda") != null && !filtros.get("textoBusqueda").toString().trim().isEmpty() 
                               ? filtros.get("textoBusqueda").toString() : null;
        String carrera = filtros.get("carrera") != null && !filtros.get("carrera").toString().trim().isEmpty() 
                         ? filtros.get("carrera").toString() : null;
        String ubicacion = filtros.get("ubicacion") != null && !filtros.get("ubicacion").toString().trim().isEmpty() 
                           ? filtros.get("ubicacion").toString() : null;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM portafolio p ");
        sql.append("INNER JOIN usuarios u ON p.id_usuario = u.id_usuario ");
        sql.append("LEFT JOIN alumnos a ON u.id_usuario = a.id_usuario ");
        sql.append("WHERE p.visibilidad = true ");
        
        if (textoBusqueda != null) {
            sql.append("AND (LOWER(u.nombre) LIKE ? OR LOWER(u.apellido) LIKE ? OR LOWER(p.skills) LIKE ?) ");
        }
        if (carrera != null) {
            sql.append("AND a.carrera IS NOT NULL AND LOWER(a.carrera) LIKE ? ");
        }
        if (ubicacion != null) {
            sql.append("AND u.ubicacion IS NOT NULL AND LOWER(u.ubicacion) LIKE ? ");
        }
        
        Query query = entityManager.createNativeQuery(sql.toString());
        
        int paramIndex = 1;
        if (textoBusqueda != null) {
            String textoPattern = "%" + textoBusqueda.toLowerCase() + "%";
            query.setParameter(paramIndex++, textoPattern);
            query.setParameter(paramIndex++, textoPattern);
            query.setParameter(paramIndex++, textoPattern);
        }
        if (carrera != null) {
            query.setParameter(paramIndex++, "%" + carrera.toLowerCase() + "%");
        }
        if (ubicacion != null) {
            query.setParameter(paramIndex++, "%" + ubicacion.toLowerCase() + "%");
        }
        
        return ((Number) query.getSingleResult()).intValue();
    }
    
    /**
     * Obtiene los filtros disponibles (carreras y ubicaciones)
     */
    public Map<String, Object> obtenerFiltros() {
        System.out.println(">>> Obteniendo filtros disponibles");
        
        // Obtener carreras únicas desde el repository
        List<String> carreras = alumnoRepository.findDistinctCarreras();
        
        // Obtener ubicaciones únicas desde el repository
        List<String> ubicaciones = usuarioRepository.findDistinctUbicaciones();
        
        Map<String, Object> filtros = new HashMap<>();
        filtros.put("carreras", carreras);
        filtros.put("ubicaciones", ubicaciones);
        
        System.out.println(">>> Carreras: " + carreras.size() + ", Ubicaciones: " + ubicaciones.size());
        
        return filtros;
    }
}
