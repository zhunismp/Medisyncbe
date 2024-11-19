package com.mahidol.drugapi.notification.repositories;

import com.mahidol.drugapi.notification.models.druggroup.DrugGroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DrugGroupScheduleRepository extends JpaRepository<DrugGroupSchedule, UUID> {
    List<DrugGroupSchedule> findByScheduledTime(LocalTime time);
}
