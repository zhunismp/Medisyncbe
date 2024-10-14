package com.mahidol.drugapi.drug.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.drug.dtos.request.CreateDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.SearchDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.UpdateDrugRequest;
import com.mahidol.drugapi.drug.dtos.response.SearchDrugResponse;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.models.type.MealCondition;
import com.mahidol.drugapi.drug.repositories.DrugRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DrugService {

    private final DrugRepository drugRepository;
    private final PaginationService<Drug> paginationService;

    public DrugService(DrugRepository drugRepository, PaginationService<Drug> paginationService) {
        this.drugRepository = drugRepository;
        this.paginationService = paginationService;
    }

    public SearchDrugResponse search(SearchDrugRequest request) {
        List<Drug> userDrugs = searchDrugByUserId(request.getUserId());

        // TODO: Add applyFilter method
        List<Drug> drugs = userDrugs.stream().filter(drug -> request.getGenericName()
                .map(gname -> drug.getGenericName().contains(gname)).orElse(true)).toList();


        return new SearchDrugResponse(applyPaginate(drugs, request.getPagination()), drugs.size());
    }

    public void add(CreateDrugRequest request) {
        boolean isDrugExists = searchDrugByUserId(request.getUserId())
                .stream()
                .map(Drug::getGenericName)
                .anyMatch(name -> name.equalsIgnoreCase(request.getGenericName()));

        if (isDrugExists)
            throw new IllegalArgumentException("Drug name already exists, please use another name");

        drugRepository.save(new Drug()
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
        );
    }

    public void update(UpdateDrugRequest request) {
        if (!validateOwner(request.getUserId(), List.of(request.getDrugId())))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        Drug target = drugRepository.findById(request.getDrugId())
                .map(drug -> drug.setGenericName(request.getGenericName().orElse(drug.getGenericName()))
                        .setDosageForm(request.getDosageForm().orElse(drug.getDosageForm()))
                        .setUnit(request.getUnit().orElse(drug.getUnit()))
                        .setStrength(request.getStrength().orElse(drug.getStrength()))
                        .setAmount(request.getAmount().orElse(drug.getAmount()))
                        .setDose(request.getDose().orElse(drug.getDose()))
                        .setUsageTime(request.getUsageTime().map(MealCondition::fromValue).orElse(drug.getUsageTime()))
                        .setSchedules(request.getSchedules().orElse(drug.getSchedules()))
                        .setIsEnable(request.getIsEnabled().orElse(drug.getIsEnable()))
                )
                .orElseThrow(() -> new EntityNotFoundException("Drug id not found with id " + request.getDrugId()));

        drugRepository.save(target);
    }

    public void remove(UUID userId, UUID drugId) {
        deleteAllByDrugIds(userId, List.of(drugId));
    }

    public List<Drug> searchAllDrugByDrugsId(UUID userId, List<UUID> drugIds) {
        if (!validateOwner(userId, drugIds))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        return drugRepository.findAllById(drugIds);
    }

    public List<Drug> saveAllDrugs(UUID userId, List<Drug> drugs) {
        if (!validateOwner(userId, drugs.stream().map(Drug::getId).toList()))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        return drugRepository.saveAll(drugs);
    }

    public void deleteAllByDrugIds(UUID userId, List<UUID> drugIds) {
        if (!validateOwner(userId, drugIds))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        drugRepository.deleteAllById(drugIds);
    }

    private Boolean validateOwner(UUID userId, List<UUID> drugIds) {
        List<UUID> validDrugIds = drugRepository.findByUserId(userId).stream().map(Drug::getId).toList();

        return new HashSet<>(validDrugIds).containsAll(drugIds);
    }

    private List<Drug> searchDrugByUserId(UUID userId) {
        return drugRepository.findByUserId(userId);
    }

    private List<Drug> applyPaginate(List<Drug> drugs, Optional<Pagination> pagination) {
        return pagination.map(p -> paginationService.paginate(drugs, p)).orElse(drugs);
    }
}
