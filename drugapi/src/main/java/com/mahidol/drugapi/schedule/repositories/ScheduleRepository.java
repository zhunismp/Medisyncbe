package com.mahidol.drugapi.schedule.repositories;

import com.mahidol.drugapi.schedule.models.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Modifying
    void deleteAllByReferenceId(UUID referenceId);

    List<Schedule> findAllByReferenceId(UUID referenceId);
}
