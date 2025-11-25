package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Empresa;
import com.vias.uc.backend.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /**
     * Busca una empresa por el ID del usuario
     */
    public Optional<Empresa> findByUsuarioId(Long usuarioId) {
        return empresaRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Actualiza una empresa
     */
    @Transactional
    public Empresa updateEmpresa(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    /**
     * Busca una empresa por ID
     */
    public Optional<Empresa> findById(Integer id) {
        return empresaRepository.findById(id);
    }
    
    /**
     * Busca una empresa por email
     */
    public Optional<Empresa> findByEmail(String email) {
        return empresaRepository.findByEmail(email);
    }
    
    /**
     * Lista todas las empresas registradas
     */
    public java.util.List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }
}
