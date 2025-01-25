package com.mahidol.drugapi.history.models.entities;

import com.mahidol.drugapi.history.models.types.TakenStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "history")
public class History {
    @Id
    private UUID id;
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;
    @Column(name = "drug_id", columnDefinition = "UUID")
    private UUID drugId;
    @Column(name = "group_id", columnDefinition = "UUID")
    private UUID groupId;
    @Column(name = "status", columnDefinition = "TEXT")
    private TakenStatus status;
    @Column(name = "taken_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime takenAt;
    @Column(name = "notified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime notifiedAt;
    @Column(name = "count", columnDefinition = "INT")
    private Integer count;
}
