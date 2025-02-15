package com.mahidol.drugapi.relation.repositories;

import com.mahidol.drugapi.relation.models.entities.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RelationRepository extends JpaRepository<Relation, UUID> {
    List<Relation> findByUserId(UUID userId);
    List<Relation> findByRelativeId(UUID relativeId);
    Boolean existsByUserIdAndRelativeId(UUID userId, UUID relativeId);

    @Modifying
    void deleteByUserIdAndRelativeId(UUID userId, UUID referenceId);
}
