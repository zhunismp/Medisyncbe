package com.mahidol.drugapi.history.repositories;

import com.mahidol.drugapi.history.models.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {
    @Query("SELECT h FROM History h WHERE h.userId = :userId AND EXTRACT(MONTH FROM h.notifiedAt) = :month AND EXTRACT(YEAR FROM h.notifiedAt) = :year")
    List<History> findByUserIdAndMonthAndYear(@Param("userId") UUID userId, @Param("month") int month, @Param("year") int year);

    @Query(value = "SELECT * FROM history h WHERE h.user_id = :userId AND h.notified_at::date = :date", nativeQuery = true)
    List<History> findByUserIdAndDate(@Param("userId") UUID userId, @Param("date") LocalDate date);

    List<History> findByUserId(UUID userId);

    @Modifying  // Required because it's a modifying query
    @Query("DELETE FROM History h WHERE h.groupId IN :drugIds")
    void deleteAllByDrugIds(@Param("drugIds") List<UUID> drugsId);
}
