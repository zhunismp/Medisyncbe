package com.mahidol.drugapi.druggroup.repositories;

import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DrugGroupRepository extends JpaRepository<DrugGroup, UUID> {
    List<DrugGroup> findByUserId(UUID userId);
}
