package com.mahidol.drugapi.drug.services;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.drug.dtos.request.AddDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.SearchDrugRequest;
import com.mahidol.drugapi.drug.dtos.response.AddDrugResponse;
import com.mahidol.drugapi.drug.dtos.response.SearchDrugResponse;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.models.type.MealCondition;
import com.mahidol.drugapi.drug.repositories.DrugRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DrugService {

    private final DrugRepository drugRepository;
    private final PaginationService<Drug> paginationService;

    public DrugService(DrugRepository drugRepository, PaginationService<Drug> paginationService) {
        this.drugRepository = drugRepository;
        this.paginationService = paginationService;
    }

    public SearchDrugResponse search(SearchDrugRequest request) {
        // TODO: Add applyFilter method
        List<Drug> drugs = drugRepository.findByUserId(request.getUserId()).stream().filter(drug -> request.getGenericName()
                .map(gname -> drug.getGenericName().contains(gname)).orElse(true)).toList();


        return new SearchDrugResponse(applyPaginate(drugs, request.getPagination()), drugs.size(), true);
    }

    public AddDrugResponse add(AddDrugRequest request) {
        UUID drugId = drugRepository.save(new Drug()
                .setUserId(request.getUserId())
                .setGenericName(request.getGenericName())
                .setDosageForm(request.getDosageForm())
                .setUnit(request.getUnit())
                .setStrength(request.getStrength())
                .setAmount(request.getAmount())
                .setDose(request.getDose())
                .setTakenAmount(request.getAmount())
                .setUsageTime(MealCondition.fromValue(request.getUsageTime()))
                .setSchedules(request.getSchedules())
                .setIsInternalDrug(request.getIsInternalDrug())
                .setIsEnable(request.getIsEnabled())
        ).getId();

        return new AddDrugResponse(
                List.of(drugId),
                1,
                true
        );

    }

    public void remove(UUID drugId) {
        if (!drugRepository.existsById(drugId))
            throw new EntityNotFoundException("Drug id not found with id " + drugId);
        drugRepository.deleteById(drugId);
    }

    private List<Drug> applyPaginate(List<Drug> drugs, Optional<Pagination> pagination) {
        return pagination.map(p -> paginationService.paginate(drugs, p)).orElse(drugs);

    }
}
