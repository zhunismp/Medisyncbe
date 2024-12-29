package com.mahidol.drugapi.notification.models.druggroup;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "drug_group_schedule")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DrugGroupSchedule {
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
    @Column(name = "drug_group_id", columnDefinition = "UUID")
    private UUID drugGroupId;
}
