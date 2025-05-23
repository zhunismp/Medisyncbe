package com.mahidol.drugapi.appointment.repositories;

import com.mahidol.drugapi.appointment.models.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByUserId(UUID userId);
    List<Appointment> findByUserIdAndDatetimeBetween(UUID userId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByUserIdAndDatetimeAfter(UUID userId, LocalDateTime start);
}
