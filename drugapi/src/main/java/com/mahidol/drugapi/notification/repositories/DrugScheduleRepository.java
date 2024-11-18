package com.mahidol.drugapi.notification.repositories;

import com.mahidol.drugapi.notification.models.drug.DrugSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DrugScheduleRepository extends JpaRepository<DrugSchedule, UUID> {
    List<DrugSchedule> findByScheduledTime(LocalTime time);
}
