package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.model.Endorsement.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EndorsementRepository extends JpaRepository<Endorsement, Long> {
    List<Endorsement> findAllByToUserIdAndStatus(Integer toUserId, Status status);
    List<Endorsement> findAllByToUserId(Integer toUserId);
    List<Endorsement> findAllByFromUserId(Integer fromUserId);
    Optional<Endorsement> findByIdEndorsementAndToUserId(Long idEndorsement, Integer toUserId);
    boolean existsByFromUserIdAndToUserIdAndSkillAndStatus(Integer fromUserId, Integer toUserId, String skill, Status status);
}
