package com.mahidol.drugapi.relation.models.entities;

import com.mahidol.drugapi.relation.models.types.Status;
import com.mahidol.drugapi.relation.models.types.StatusConvertor;
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
@Table(name = "relationship")
public class Relation {
    @Id
    private UUID id;
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;
    @Column(name = "relative_id", columnDefinition = "UUID")
    private UUID relativeId;
    @Column(name = "relation", columnDefinition = "TEXT")
    private String relation;
    @Convert(converter = StatusConvertor.class)
    @Column(name = "status", columnDefinition = "TEXT")
    private Status status;
    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;
}
