package com.mahidol.drugapi.Drug.services;

import com.mahidol.drugapi.Drug.entites.InternalDrug;
import com.mahidol.drugapi.Drug.repositories.InternalDrugRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final InternalDrugRepository internalDrugRepository;

    public SearchService(InternalDrugRepository internalDrugRepository) {
        this.internalDrugRepository = internalDrugRepository;
    }

    public List<InternalDrug> searchInternalDrug() {
        return internalDrugRepository.findAll();
    }

    public List<InternalDrug> searchInternalDrugByGenericName(String genericName) {
        return internalDrugRepository.findByGenericNameContaining(genericName);
    }

}
