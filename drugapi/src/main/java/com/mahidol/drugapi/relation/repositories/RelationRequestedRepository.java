package com.mahidol.drugapi.relation.repositories;

import com.mahidol.drugapi.relation.models.entities.RelationRequested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RelationRequestedRepository extends JpaRepository<RelationRequested, UUID> {
    List<RelationRequested> findByUserId(UUID userId);
    List<RelationRequested> findByRelativeId(UUID relativeId);
    Boolean existsByUserIdAndRelativeId(UUID userId, UUID relativeId);
}
