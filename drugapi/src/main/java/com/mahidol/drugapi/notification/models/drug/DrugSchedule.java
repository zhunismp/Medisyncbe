package com.mahidol.drugapi.notification.models.drug;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "drug_schedule")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DrugSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "device_id", columnDefinition = "TEXT")
    private String deviceId;
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;
    @Column(name = "schedule_time", columnDefinition = "TIME")
    private LocalTime scheduledTime;
    @Column(name = "drug_id", columnDefinition = "UUID")
    private UUID drugId;
}
