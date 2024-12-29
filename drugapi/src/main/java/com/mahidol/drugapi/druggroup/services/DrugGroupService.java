package com.mahidol.drugapi.druggroup.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.models.Schedule;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.dtos.request.AddDrugRequest;
import com.mahidol.drugapi.druggroup.dtos.request.CreateGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.request.SearchGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
import com.mahidol.drugapi.druggroup.dtos.response.SearchGroupResponse;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.druggroup.repositories.DrugGroupRepository;
import com.mahidol.drugapi.notification.models.druggroup.DrugGroupSchedule;
import com.mahidol.drugapi.notification.repositories.DrugGroupScheduleRepository;
import com.mahidol.drugapi.notification.repositories.DrugScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DrugGroupService {
    private final DrugGroupRepository drugGroupRepository;
    private final DrugGroupScheduleRepository drugGroupScheduleRepository;
    private final DrugScheduleRepository drugScheduleRepository;
    private final DrugService drugService;
    private final PaginationService<DrugGroupDTO> paginationService;

    public DrugGroupService(
            DrugGroupRepository drugGroupRepository,
            DrugGroupScheduleRepository drugGroupScheduleRepository,
            DrugScheduleRepository drugScheduleRepository,
            DrugService drugService,
            PaginationService<DrugGroupDTO> paginationService
    ) {
        this.drugGroupRepository = drugGroupRepository;
        this.drugGroupScheduleRepository = drugGroupScheduleRepository;
        this.drugScheduleRepository = drugScheduleRepository;
        this.drugService = drugService;
        this.paginationService = paginationService;
    }

    public void create(CreateGroupRequest request) {
        boolean groupNameExists = drugGroupRepository.findByUserId(request.getUserId())
                .stream()
                .map(DrugGroup::getGroupName)
                .anyMatch(name -> name.equalsIgnoreCase(request.getGroupName()));

        if (groupNameExists)
            throw new IllegalArgumentException("Group name already exists, please use another name");

        DrugGroup group = new DrugGroup()
                .setUserId(request.getUserId())
                .setGroupName(request.getGroupName())
//                .setSchedules(request.getSchedules())
                .setDrugs(request.getDrugs());
//                .setIsEnabled(request.getIsEnabled());


        // disable notification flag for individual drug
        setDrugNotifications(false, group.getUserId(), group.getDrugs(), request.getDeviceToken());
//        drugService.saveAllDrugs(
//                request.getUserId(),
//                drugService.searchAllDrugByDrugsId(request.getUserId(), request.getDrugs()).stream()
//                        .map(drug -> drug.setIsEnable(false)).toList()
//        );
        DrugGroup savedGroup = drugGroupRepository.save(group);
        scheduleDrugGroup(savedGroup, request.getSchedules(), request.getDeviceToken());
    }

    public SearchGroupResponse search(SearchGroupRequest request) {
        List<DrugGroup> drugGroups = drugGroupRepository.findByUserId(request.getUserId());
        List<DrugGroupDTO> drugGroupWithDrugInfos = drugGroups.stream()
                .map(drugGroup -> DrugGroupDTO.fromDrugGroup(drugGroup, drugsId -> drugService.searchAllDrugByDrugsId(request.getUserId(), drugsId)))
                .toList();

        return new SearchGroupResponse(
                applyPaginate(drugGroupWithDrugInfos, request.getPagination()),
                drugGroupWithDrugInfos.size()
        );
    }

    public List<DrugGroupDTO> searchAllDrugGroupByDrugGroupIds(List<UUID> drugGroupIds) {
        List<DrugGroup> drugGroups = drugGroupRepository.findAllById(drugGroupIds);

        return drugGroups.stream()
                .map(drugGroup -> DrugGroupDTO.fromDrugGroup(drugGroup, drugsId -> drugService.searchAllDrugByDrugsId(null, drugsId)))
                .toList();
    }


    public void remove(UUID drugGroupId, UUID userId, String deviceToken, Boolean isRemoveDrug) {
        List<UUID> drugIds = getDrugGroupByGroupId(userId, drugGroupId).getDrugs();

        if (isRemoveDrug)
            drugService.deleteAllByDrugIds(userId, drugIds);
        else
            // After remove drug from the drug group, we need to set isEnabled to true again
            // This is for make old notification of drug behavior the same as before.
            setDrugNotifications(true, userId, drugIds, deviceToken);
//            drugService.saveAllDrugs(userId, drugService.searchAllDrugByDrugsId(userId, drugIds).stream()
//                    .map(drug -> drug.setIsEnable(true)).toList());

        drugGroupRepository.deleteById(drugGroupId);
    }

    public void addDrugsToGroup(AddDrugRequest request) {
        DrugGroup drugGroup = getDrugGroupByGroupId(request.getUserId(), request.getGroupId());
//        List<Drug> drugs = drugService.searchAllDrugByDrugsId(request.getUserId(), request.getDrugs()).stream().map(drug -> drug.setIsEnable(false)).toList();

        // disable notification flag for individual drug
        setDrugNotifications(false, request.getUserId(), request.getDrugs(), request.getDeviceToken());
//        drugService.saveAllDrugs(request.getUserId(), drugs.stream().map(d -> d.setIsEnable(false)).toList());
        drugGroupRepository.save(drugGroup.setDrugs(
                Stream.concat(drugGroup.getDrugs().stream(), request.getDrugs().stream()).collect(Collectors.toList())
        ));
    }

    private void scheduleDrugGroup(DrugGroup drugGroup, List<Schedule> schedules, String deviceToken) {
        drugGroupScheduleRepository.deleteAllByDrugGroupId(drugGroup.getId());
        List<DrugGroupSchedule> drugGroupSchedules = schedules.stream().map(
                s -> new DrugGroupSchedule()
                        .setDrugGroupId(drugGroup.getId())
                        .setScheduledTime(s.getTime())
                        .setIsEnabled(s.getIsEnabled())
                        .setUserId(drugGroup.getUserId())
                        .setDeviceToken(deviceToken)
        ).toList();

        drugGroupScheduleRepository.saveAll(drugGroupSchedules);
    }

    private DrugGroup getDrugGroupByGroupId(UUID userId, UUID groupId) {
        return drugGroupRepository.findById(groupId)
                .map(dg -> {
                    if (validateOwner(userId, List.of(dg.getId())))
                        return dg;
                    else throw new IllegalArgumentException("User not own drug group id: " + groupId);
                })
                .orElseThrow(() -> new EntityNotFoundException("User try to add some drug to non exists drug group"));
    }

    private List<DrugGroupDTO> applyPaginate(List<DrugGroupDTO> drugGroups, Optional<Pagination> pagination) {
        return pagination.map(p -> paginationService.paginate(drugGroups, p)).orElse(drugGroups);
    }

    private Boolean validateOwner(UUID userId, List<UUID> drugGroupIds) {
        List<UUID> validDrugGroupIds = drugGroupRepository.findByUserId(userId).stream().map(DrugGroup::getId).toList();

        return new HashSet<>(validDrugGroupIds).containsAll(drugGroupIds);
    }

    private void setDrugNotifications(Boolean isEnabled, UUID userId, List<UUID> drugIds, String deviceToken) {
        List<Drug> drugs = drugService.searchAllDrugByDrugsId(userId, drugIds);
        drugs.forEach(drug -> {
            List<Schedule> drugSchedules = drugScheduleRepository.findByDrugId(drug.getId()).stream()
                    .map(d -> d.setIsEnabled(isEnabled)).toList();
            drugService.scheduledDrug(drug, drugSchedules, deviceToken);
        });
    }
}
