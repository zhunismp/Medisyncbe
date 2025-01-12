package com.mahidol.drugapi.drug.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.models.Schedule;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.drug.dtos.request.CreateDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.SearchDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.UpdateDrugRequest;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.dtos.response.SearchDrugResponse;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.models.type.MealCondition;
import com.mahidol.drugapi.drug.repositories.DrugRepository;
import com.mahidol.drugapi.external.aws.s3.S3Service;
import com.mahidol.drugapi.notification.models.drug.DrugSchedule;
import com.mahidol.drugapi.notification.repositories.DrugScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DrugService {

    private final DrugRepository drugRepository;
    private final DrugScheduleRepository drugScheduleRepository;
    private final PaginationService<DrugDTO> paginationService;
    private final UserContext userContext;
    private final S3Service s3Service;

    public DrugService(
            DrugRepository drugRepository,
            DrugScheduleRepository drugScheduleRepository,
            PaginationService<DrugDTO> paginationService,
            UserContext userContext,
            S3Service s3Service
    ) {
        this.drugRepository = drugRepository;
        this.drugScheduleRepository = drugScheduleRepository;
        this.paginationService = paginationService;
        this.userContext = userContext;
        this.s3Service = s3Service;
    }

    public SearchDrugResponse search(SearchDrugRequest request) {
        List<Drug> userDrugs = searchDrugByUserId(userContext.getUserId());

        // TODO: Add applyFilter method
        List<Drug> filteredDrugs = userDrugs.stream().filter(drug -> request.getGenericName()
                .map(gname -> drug.getGenericName().contains(gname)).orElse(true)).toList();

        List<DrugDTO> response = filteredDrugs.stream().map(drug -> {
//            Optional<String> drugImageUrl = s3Service.getUrl("medisync-drug", drug.getId().toString());
            return DrugDTO.fromDrug(drug, Optional.empty());
        }).toList();

        return new SearchDrugResponse(applyPaginate(response, request.getPagination()), filteredDrugs.size());
    }

    public void add(CreateDrugRequest request) {
        boolean isDrugExists = searchDrugByUserId(userContext.getUserId())
                .stream()
                .map(Drug::getGenericName)
                .anyMatch(name -> name.equalsIgnoreCase(request.getGenericName()));

        if (isDrugExists)
            throw new IllegalArgumentException("Drug name already exists, please use another name");

        Drug savedDrug = drugRepository.save(new Drug()
                .setUserId(userContext.getUserId())
                .setGenericName(request.getGenericName())
                .setDosageForm(request.getDosageForm())
                .setUnit(request.getUnit())
                .setStrength(request.getStrength())
                .setAmount(request.getAmount())
                .setDose(request.getDose())
                .setTakenAmount(request.getAmount())
                .setUsageTime(MealCondition.fromValue(request.getUsageTime()))
                .setIsInternalDrug(request.getIsInternalDrug())
        );

//        scheduledDrug(savedDrug, request.getSchedules(), userContext.getDeviceToken());
        request.getImage().map(file -> s3Service.uploadFile("medisync-drug", savedDrug.getId().toString(), file));
    }

    public void update(UpdateDrugRequest request) {
        if (!validateOwner(userContext.getUserId(), List.of(request.getDrugId())))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        Drug target = drugRepository.findById(request.getDrugId())
                .map(drug -> drug.setGenericName(request.getGenericName().orElse(drug.getGenericName()))
                        .setDosageForm(request.getDosageForm().orElse(drug.getDosageForm()))
                        .setUnit(request.getUnit().orElse(drug.getUnit()))
                        .setStrength(request.getStrength().orElse(drug.getStrength()))
                        .setAmount(request.getAmount().orElse(drug.getAmount()))
                        .setDose(request.getDose().orElse(drug.getDose()))
                        .setUsageTime(request.getUsageTime().map(MealCondition::fromValue).orElse(drug.getUsageTime()))
                )
                .orElseThrow(() -> new EntityNotFoundException("Drug id not found with id " + request.getDrugId()));

        drugRepository.save(target);
//        request.getSchedules().ifPresent(schedules -> scheduledDrug(target, schedules, request.getDeviceToken()));
        request.getImage().map(file -> s3Service.uploadFile("medisync-drug", request.getDrugId().toString(), file));
    }

    public void remove(UUID drugId) {
        drugScheduleRepository.deleteAllByDrugId(drugId);
        deleteAllByDrugIds(userContext.getUserId(), List.of(drugId));
    }

    public List<Drug> searchAllDrugByDrugsId(UUID userId, List<UUID> drugIds) {
        if (userId != null && !validateOwner(userId, drugIds))
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

    public void scheduledDrug(Drug drug, List<Schedule> schedules, String deviceToken) {
        // remove old schedule.
        drugScheduleRepository.deleteAllByDrugId(drug.getId());

        List<DrugSchedule> drugSchedules = schedules.stream().map(
                s -> new DrugSchedule()
                        .setDrugId(drug.getId())
                        .setScheduledTime(s.getTime())
                        .setIsEnabled(s.getIsEnabled())
                        .setUserId(drug.getUserId())
                        .setDeviceToken(deviceToken)
        ).toList();

        drugScheduleRepository.saveAll(drugSchedules);
    }

    private Boolean validateOwner(UUID userId, List<UUID> drugIds) {
        List<UUID> validDrugIds = drugRepository.findByUserId(userId).stream().map(Drug::getId).toList();

        return new HashSet<>(validDrugIds).containsAll(drugIds);
    }

    private List<Drug> searchDrugByUserId(UUID userId) {
        return drugRepository.findByUserId(userId);
    }

    private List<DrugDTO> applyPaginate(List<DrugDTO> drugs, Optional<Pagination> pagination) {
        return pagination.map(p -> paginationService.paginate(drugs, p)).orElse(drugs);
    }
}
