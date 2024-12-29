package com.mahidol.drugapi.druggroup.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "druggroup")
public class DrugGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "group_name", columnDefinition = "TEXT")
    private String groupName;

//    @Column(name = "schedule", columnDefinition = "TIME[]")
//    private List<LocalTime> schedules;

    @Column(name = "drug_id", columnDefinition = "UUID[]")
    private List<UUID> drugs;

//    @Column(name = "is_enable", columnDefinition = "BOOLEAN")
//    private Boolean isEnabled;
}
