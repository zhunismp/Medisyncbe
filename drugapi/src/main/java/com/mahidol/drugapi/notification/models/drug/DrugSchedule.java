package com.mahidol.drugapi.notification.models.drug;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "drug_schedule")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DrugSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "device_token", columnDefinition = "TEXT")
    private String deviceToken;
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;
    @Column(name = "schedule_time", columnDefinition = "TIME")
    private LocalTime scheduledTime;
    @Column(name = "is_enabled", columnDefinition = "BOOLEAN")
    private Boolean isEnabled;
    @Column(name = "drug_id", columnDefinition = "UUID")
    private UUID drugId;
}
