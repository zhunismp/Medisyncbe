package com.mahidol.drugapi.Drug.repositories;


import com.mahidol.drugapi.Drug.entites.InternalDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InternalDrugRepository extends JpaRepository<InternalDrug, UUID> {
    List<InternalDrug> findByGenericNameContaining(String genericName);
}
