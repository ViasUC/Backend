package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.repository.OportunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BolsaTrabajoService {

    private final OportunidadRepository oportunidadRepository;

    @Autowired
    public BolsaTrabajoService(OportunidadRepository oportunidadRepository) {
        this.oportunidadRepository = oportunidadRepository;
    }

    /**
     * Implementación de F4: Busca oportunidades filtradas dinámicamente.
     * @param ubicacion Filtro opcional por ubicación (ej: "Asunción")
     * @param modalidad Filtro opcional por modalidad (ej: "Remoto")
     * @param nombreEmpresa Filtro opcional por empresa (ej: "Tigo")
     * @return Lista de oportunidades que coinciden con los filtros.
     */
    @Transactional(readOnly = true)
    public List<Oportunidad> consultarOportunidades(String ubicacion, String modalidad, String nombreEmpresa) {
        // Usamos Specification.where() para construir la consulta dinámicamente
        Specification<Oportunidad> spec = (root, query, criteriaBuilder) -> {
            root.fetch("empresa", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro: solo oportunidades activas
            predicates.add(criteriaBuilder.equal(root.get("estado"), "activo"));

            // 2. Filtro: ubicación
            if (ubicacion != null && !ubicacion.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("ubicacion"), "%" + ubicacion + "%"));
            }

            // 3. Filtro: modalidad
            if (modalidad != null && !modalidad.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("modalidad"), modalidad));
            }

            // 4. Filtro: nombre de la empresa
            if (nombreEmpresa != null && !nombreEmpresa.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("empresa").get("nombreEmpresa"),
                        nombreEmpresa
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Oportunidad> oportunidades = oportunidadRepository.findAll(spec);

        // Llamada “trivial” para reflejar el mensaje del diagrama
        return mostrarOportunidades(oportunidades);
    }

    /**
     * (→ “mostrarOportunidades(datos)” en el diagrama)
     * Esta función no cambia la lógica; simplemente representa la “respuesta” del backend.
     */
    private List<Oportunidad> mostrarOportunidades(List<Oportunidad> oportunidades) {
        System.out.println("mostrarOportunidades(): " + oportunidades.size() + " resultados encontrados");
        return oportunidades;
    }
}
