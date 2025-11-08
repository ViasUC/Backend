package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "endorsement")
public class Endorsement {
    public enum Status { PENDING, ACCEPTED, REJECTED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endorsement")
    private Long idEndorsement;

    @Column(name = "from_user_id", nullable = false)
    private Integer fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private Integer toUserId;

    @Column(name = "skill")
    private String skill;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "decided_at")
    private Instant decidedAt;

    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;

    // getters/setters
    public Long getIdEndorsement() { return idEndorsement; }
    public void setIdEndorsement(Long idEndorsement) { this.idEndorsement = idEndorsement; }
    public Integer getFromUserId() { return fromUserId; }
    public void setFromUserId(Integer fromUserId) { this.fromUserId = fromUserId; }
    public Integer getToUserId() { return toUserId; }
    public void setToUserId(Integer toUserId) { this.toUserId = toUserId; }
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getDecidedAt() { return decidedAt; }
    public void setDecidedAt(Instant decidedAt) { this.decidedAt = decidedAt; }
    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }
}
