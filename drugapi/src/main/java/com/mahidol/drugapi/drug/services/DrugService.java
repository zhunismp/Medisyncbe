package com.mahidol.drugapi.drug.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.drug.dtos.DrugDTOMapper;
import com.mahidol.drugapi.drug.dtos.request.CreateDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.SearchDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.UpdateDrugRequest;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.dtos.response.SearchDrugResponse;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.models.type.MealCondition;
import com.mahidol.drugapi.drug.repositories.DrugRepository;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.druggroup.repositories.DrugGroupRepository;
import com.mahidol.drugapi.external.aws.s3.S3Service;
import com.mahidol.drugapi.relation.services.RelationService;
import com.mahidol.drugapi.schedule.services.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DrugService {

    private final DrugRepository drugRepository;
    private final DrugGroupRepository drugGroupRepository; // TODO: this service shouldn't inject repo for other contexts.
    private final ScheduleService scheduleService;
    private final RelationService relationService;
    private final PaginationService<DrugDTO> paginationService;
    private final UserContext userContext;
    private final S3Service s3Service;

    public DrugService(
            DrugRepository drugRepository,
            DrugGroupRepository drugGroupRepository,
            ScheduleService scheduleService,
            RelationService relationService,
            PaginationService<DrugDTO> paginationService,
            UserContext userContext,
            S3Service s3Service
    ) {
        this.drugRepository = drugRepository;
        this.drugGroupRepository = drugGroupRepository;
        this.scheduleService = scheduleService;
        this.relationService = relationService;
        this.paginationService = paginationService;
        this.userContext = userContext;
        this.s3Service = s3Service;
    }

    public SearchDrugResponse search(SearchDrugRequest request) {
        UUID id = request.getRelativeId().map(i -> {
            if (!relationService.getIncomingPermission(i).getReadable())
                throw new IllegalArgumentException("Access denied from your friend");

            return i;
        }).orElse(userContext.getUserId());

        List<Drug> userDrugs = searchDrugByUserId(id);

        // TODO: Add applyFilter method
        List<Drug> filteredDrugs = userDrugs.stream().filter(drug -> request.getGenericName()
                .map(gname -> drug.getGenericName().contains(gname)).orElse(true)).toList();

        //            Optional<String> drugImageUrl = s3Service.getUrl("medisync-drug", drug.getId().toString());
        List<DrugDTO> response = filteredDrugs.stream().map(this::transformDTO)
                .sorted(Comparator.comparing(DrugDTO::getGenericName))
                .collect(Collectors.toList());

        return new SearchDrugResponse(applyPaginate(response, request.getPagination()), filteredDrugs.size());
    }

    public void create(CreateDrugRequest request) {
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
                .setTakenAmount(0.0)
                .setUsageTime(MealCondition.fromValue(request.getUsageTime()))
                .setIsInternalDrug(request.getIsInternalDrug())
        );

        // TODO: Fixed AWS service
//         request.getImage().map(file -> s3Service.uploadFile("medisync-drug", savedDrug.getId().toString(), file));

        request.getGroups().ifPresentOrElse(
                groupIds -> {
                    linkGroup(savedDrug, groupIds);
                    scheduleService.set(savedDrug, request.getScheduleTimes().stream().map(s -> s.setIsEnabled(false)).toList());
                },
                () -> scheduleService.set(savedDrug, request.getScheduleTimes())
        );
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
                        .setIsArchived(drug.getAmount() - drug.getTakenAmount() <= 0)
                )
                .orElseThrow(() -> new EntityNotFoundException("Drug id not found with id " + request.getDrugId()));

        drugRepository.save(target);
        request.getScheduleTimes().ifPresent(s -> scheduleService.set(target, s));
//        request.getImage().map(file -> s3Service.uploadFile("medisync-drug", request.getDrugId().toString(), file));
    }

    public void remove(UUID drugId) {
        Optional<Drug> drugOpt = drugRepository.findById(drugId);

        // Remove from group before delete.
        drugOpt.ifPresent(this::unlinkGroup);

        // delete drugs
        deleteAllByDrugIds(userContext.getUserId(), List.of(drugId));
        scheduleService.remove(drugId);
    }

    public List<Drug> searchAllDrugByDrugsId(UUID userId, List<UUID> drugIds) {
        if (userId != null && !validateOwner(userId, drugIds))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        return drugRepository.findAllById(drugIds);
    }

    public Optional<Drug> searchDrugByDrugId(UUID userId, UUID drugId) {
        if (userId != null && !validateOwner(userId, List.of(drugId)))
            throw new IllegalArgumentException("User is not the owner of requested drug.");

        return drugRepository.findById(drugId);
    }

    public List<Drug> searchDrugByUserId(UUID userId) {
        return drugRepository.findByUserId(userId);
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

    private DrugDTO transformDTO(Drug drug) {
        List<ScheduleTime> scheduleTimes = scheduleService.get(drug.getId()).stream()
                .map(s -> new ScheduleTime(s.getScheduleTime().toLocalTime(), s.getIsEnabled())).toList();

        return DrugDTOMapper.toDTO(drug, scheduleTimes);
    }

    private Boolean validateOwner(UUID userId, List<UUID> drugIds) {
        List<UUID> validDrugIds = drugRepository.findByUserId(userId).stream().map(Drug::getId).toList();

        return new HashSet<>(validDrugIds).containsAll(drugIds);
    }

    private void linkGroup(Drug d, List<UUID> groupIds) {
        List<DrugGroup> groups = drugGroupRepository.findAllById(groupIds)
                .stream().map(g -> g.addDrug(d)).toList();

        drugGroupRepository.saveAll(groups);
    }

    private void unlinkGroup(Drug d) {
        List<DrugGroup> groups = d.getGroups().stream().map(g -> g.removeDrug(d)).toList();
        drugGroupRepository.saveAll(groups);
    }

    private List<DrugDTO> applyPaginate(List<DrugDTO> drugs, Optional<Pagination> pagination) {
        return pagination.map(p -> paginationService.paginate(drugs, p)).orElse(drugs);
    }
}
