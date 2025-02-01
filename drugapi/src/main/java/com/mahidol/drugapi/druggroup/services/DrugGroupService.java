package com.mahidol.drugapi.druggroup.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.dtos.request.AddDrugRequest;
import com.mahidol.drugapi.druggroup.dtos.request.CreateGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.request.SearchGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
import com.mahidol.drugapi.druggroup.dtos.response.SearchGroupResponse;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.druggroup.repositories.DrugGroupRepository;
import com.mahidol.drugapi.schedule.services.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DrugGroupService {
    private final DrugGroupRepository drugGroupRepository;
    private final DrugService drugService;
    private final ScheduleService scheduleService;
    private final PaginationService<DrugGroupDTO> paginationService;
    private final UserContext userContext;

    public DrugGroupService(
            DrugGroupRepository drugGroupRepository,
            DrugService drugService,
            ScheduleService scheduleService,
            PaginationService<DrugGroupDTO> paginationService,
            UserContext userContext
    ) {
        this.drugGroupRepository = drugGroupRepository;
        this.drugService = drugService;
        this.scheduleService = scheduleService;
        this.paginationService = paginationService;
        this.userContext = userContext;
    }

    public void create(CreateGroupRequest request) {
        boolean groupNameExists = drugGroupRepository.findByUserId(userContext.getUserId())
                .stream()
                .map(DrugGroup::getGroupName)
                .anyMatch(name -> name.equalsIgnoreCase(request.getGroupName()));

        if (groupNameExists)
            throw new IllegalArgumentException("Group name already exists, please use another name");

        DrugGroup group = new DrugGroup()
                .setUserId(userContext.getUserId())
                .setGroupName(request.getGroupName())
                .setDrugs(request.getDrugs());

        // disable notification flag for individual drug
        request.getDrugs().forEach(id -> scheduleService.setIsEnabled(id, false));

        DrugGroup savedGroup = drugGroupRepository.save(group);
        scheduleService.set(group, request.getScheduleTimes());
    }

    public SearchGroupResponse search(SearchGroupRequest request) {
        List<DrugGroup> drugGroups = drugGroupRepository.findByUserId(userContext.getUserId());
        List<DrugGroupDTO> drugGroupWithDrugInfos = drugGroups.stream()
                .map(drugGroup -> DrugGroupDTO.fromDrugGroup(drugGroup, drugsId -> drugService.searchAllDrugByDrugsId(userContext.getUserId(), drugsId)))
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

    public Optional<DrugGroup> getDrugGroupByGroupIdOpt(UUID userId, UUID groupId) {
        if (!validateOwner(userId, List.of(groupId)))
            throw new IllegalArgumentException("User is not the owner of requested drug group.");

        return drugGroupRepository.findById(groupId);
    }


    public void remove(UUID drugGroupId, Boolean isRemoveDrug) {
        List<UUID> drugIds = getDrugGroupByGroupId(userContext.getUserId(), drugGroupId).getDrugs();

        if (isRemoveDrug)
            drugService.deleteAllByDrugIds(userContext.getUserId(), drugIds);
        else
            // After remove drug from the drug group, we need to set isEnabled to true again
            // This is for make old notification of drug behavior the same as before.
            drugIds.forEach(id -> scheduleService.setIsEnabled(id, true));

        drugGroupRepository.deleteById(drugGroupId);
    }

    public void addDrugsToGroup(AddDrugRequest request) {
        DrugGroup drugGroup = getDrugGroupByGroupId(userContext.getUserId(), request.getGroupId());

        // disable notification flag for individual drug
        request.getDrugs().forEach(id -> scheduleService.setIsEnabled(id, false));

        drugGroupRepository.save(drugGroup.setDrugs(
                Stream.concat(drugGroup.getDrugs().stream(), request.getDrugs().stream()).collect(Collectors.toList())
        ));
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
}
