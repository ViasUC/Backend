package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que mapea la tabla 'empresas'.
 * Representa a una compañía que puede publicar oportunidades.
 */
@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "nombre_empresa", nullable = false)
    private String nombreEmpresa;

    @Column(name = "ruc", nullable = false)
    private String ruc;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "contacto")
    private String contacto;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "email")
    private String email;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;

    /**
     * Relación inversa para todas las oportunidades publicadas por esta empresa.
     * 'mappedBy = "empresa"' se refiere al campo 'empresa' en la clase Oportunidad.
     */
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Oportunidad> oportunidades;

    @Override
    public String toString() {
        return "Empresa{" +
                "idEmpresa=" + idEmpresa +
                ", nombreEmpresa='" + nombreEmpresa + '\'' +
                ", ruc='" + ruc + '\'' +
                '}';
    }
}