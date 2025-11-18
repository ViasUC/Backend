package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Endorsement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndorsementRepository extends JpaRepository<Endorsement, Integer> {

    List<Endorsement> findByIdUsuarioEmisor(Integer idEmisor);

    List<Endorsement> findByIdUsuarioReceptor(Integer idReceptor);
}
