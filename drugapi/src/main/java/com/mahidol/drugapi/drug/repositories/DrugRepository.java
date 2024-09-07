package com.mahidol.drugapi.drug.repositories;


import com.mahidol.drugapi.drug.models.entites.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DrugRepository extends JpaRepository<Drug, UUID> {
    List<Drug> findByUserId(UUID userId);
}
