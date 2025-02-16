package com.mahidol.drugapi.relation.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "relationship_requested")
public class RelationRequested {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;
    @Column(name = "relative_id", columnDefinition = "UUID")
    private UUID relativeId;
    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;
}
