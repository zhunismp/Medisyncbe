package com.mahidol.drugapi.schedule.repositories;

import com.mahidol.drugapi.schedule.models.entities.Schedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Transactional
    @Modifying
    void deleteAllByReferenceId(UUID referenceId);

    @Transactional
    @Modifying
    @Query("UPDATE Schedule s SET s.isEnabled = :isEnabled WHERE s.referenceId IN :referenceIds")
    void updateActiveStatusByReferenceIdIn(
            @Param("referenceIds") List<UUID> referenceIds,
            @Param("isEnabled") boolean isEnabled
    );

    List<Schedule> findAllByReferenceId(UUID referenceId);
}
