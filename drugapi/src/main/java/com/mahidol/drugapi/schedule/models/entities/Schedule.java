package com.mahidol.drugapi.schedule.models.entities;

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
@Table(name = "schedule")
public class Schedule {
    @Id
    private UUID id;

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "schedule_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime scheduleTime;

    @Column(name = "type", columnDefinition = "INT")
    private Integer type;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "reference_id", columnDefinition = "UUID")
    private UUID referenceId;

    @Column(name = "is_enabled", columnDefinition = "BOOLEAN")
    private Boolean isEnabled;
}
