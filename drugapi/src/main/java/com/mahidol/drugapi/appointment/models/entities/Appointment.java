package com.mahidol.drugapi.appointment.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "medic_name", columnDefinition = "TEXT")
    private String medicName;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "time", columnDefinition = "TIME")
    private LocalTime time;

    @Column(name = "destination", columnDefinition = "TEXT")
    private String destination;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
