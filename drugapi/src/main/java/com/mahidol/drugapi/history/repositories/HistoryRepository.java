package com.mahidol.drugapi.history.repositories;

import com.mahidol.drugapi.history.models.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {
}
