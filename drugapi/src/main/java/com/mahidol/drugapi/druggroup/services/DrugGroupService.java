package com.mahidol.drugapi.druggroup.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.drug.dtos.DrugDTOMapper;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.dtos.request.*;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
import com.mahidol.drugapi.druggroup.dtos.response.SearchGroupResponse;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.druggroup.repositories.DrugGroupRepository;
import com.mahidol.drugapi.relation.services.RelationService;
import com.mahidol.drugapi.schedule.services.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DrugGroupService {
    private final DrugGroupRepository drugGroupRepository;
    private final DrugService drugService;
    private final ScheduleService scheduleService;
    private final RelationService relationService;
    private final PaginationService<DrugGroupDTO> paginationService;
    private final UserContext userContext;

    public DrugGroupService(
            DrugGroupRepository drugGroupRepository,
            DrugService drugService,
            ScheduleService scheduleService,
            RelationService relationService,
            PaginationService<DrugGroupDTO> paginationService,
            UserContext userContext
    ) {
        this.drugGroupRepository = drugGroupRepository;
        this.drugService = drugService;
        this.scheduleService = scheduleService;
        this.relationService = relationService;
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
                .setGroupName(request.getGroupName());
        
        linkDrug(request.getDrugs(), group.getId());
        DrugGroup savedGroup = drugGroupRepository.save(group);
        scheduleService.set(savedGroup, request.getScheduleTimes());
    }

    public void update(UpdateGroupRequest request) {
        DrugGroup target = drugGroupRepository.findById(request.getGroupId())
                .map(d -> {
                    if (!d.getUserId().equals(userContext.getUserId()))
                        throw new IllegalArgumentException("User is not the owner of the group");

                    // update schedules
                    request.getScheduleTimes().ifPresent(s -> scheduleService.set(d, s));

                    return d.setGroupName(request.getGroupName().orElse(d.getGroupName()));
                })
                .orElseThrow(() -> new EntityNotFoundException("Drug group not found"));

        drugGroupRepository.save(target);
    }

    public SearchGroupResponse search(SearchGroupRequest request) {
        UUID id = request.getRelativeId().map(i -> {
            if (!relationService.getIncomingPermission(i).getReadable())
                throw new IllegalArgumentException("Access denied from your friend");

            return i;
        }).orElse(userContext.getUserId());

        List<DrugGroup> drugGroups = drugGroupRepository.findByUserId(id);
        List<DrugGroupDTO> drugGroupWithDrugInfos = drugGroups.stream()
                .map(this::transformDTO)
                .toList();

        return new SearchGroupResponse(
                applyPaginate(drugGroupWithDrugInfos, request.getPagination()),
                drugGroupWithDrugInfos.size()
        );
    }

    public void addDrugsToGroup(AddDrugRequest request) {
        linkDrug(request.getDrugs(), request.getGroupId());
    }

    public void removeDrugsFromGroup(RemoveDrugRequest request) {
        List<UUID> drugIds = request.getDrugs();

        if (request.getIsRemoveDrug()) {
            drugIds.forEach(scheduleService::remove); // remove schedule
            drugService.deleteAllByDrugIds(userContext.getUserId(), request.getDrugs()); // remove drug
            return;
        }

        unlinkDrug(drugIds, request.getGroupId());
    }

    public void remove(UUID drugGroupId, Boolean isRemoveDrug) {
        List<UUID> drugIds = getDrugGroupByGroupId(userContext.getUserId(), drugGroupId).getDrugs().stream().map(Drug::getId).toList();

        if (isRemoveDrug)
            drugService.deleteAllByDrugIds(userContext.getUserId(), drugIds);
        else
            unlinkDrug(drugIds, drugGroupId);

        drugGroupRepository.deleteById(drugGroupId);
    }

    public List<DrugGroupDTO> searchAllDrugGroupByDrugGroupIds(List<UUID> drugGroupIds) {
        List<DrugGroup> drugGroups = drugGroupRepository.findAllById(drugGroupIds);

        return drugGroups.stream()
                .map(this::transformDTO)
                .toList();
    }

    public Optional<DrugGroup> getDrugGroupByGroupIdOpt(UUID userId, UUID groupId) {
        if (!validateOwner(userId, List.of(groupId)))
            throw new IllegalArgumentException("User is not the owner of requested drug group.");

        return drugGroupRepository.findById(groupId);
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

    // After remove drug from the drug group, we need to set isEnabled to true again
    // This is for make old notification of drug behavior the same as before.
    private void unlinkDrug(List<UUID> drugIds, UUID groupId) {
        DrugGroup target = drugGroupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not found"));
        List<Drug> drugs = drugService.searchAllDrugByDrugsId(userContext.getUserId(), drugIds);

        // Remove all drug from group
        List<Drug> initial = target.getDrugs();
        initial.removeAll(drugs);
        drugGroupRepository.save(target.setDrugs(initial));

        // scheduler
        drugIds.forEach(id -> scheduleService.setIsEnabled(id, true));
    }

    private void linkDrug(List<UUID> drugIds, UUID groupId) {
        // skip link is drugIds is empty
        if (drugIds.isEmpty())
            return;

        DrugGroup target = drugGroupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not found"));
        List<Drug> drugs = drugService.searchAllDrugByDrugsId(userContext.getUserId(), drugIds);

        // Add all current drugs in group
        drugs.addAll(target.getDrugs());

        // Deduplicated
        List<Drug> deduplicated = drugs.stream().distinct().toList();
        drugGroupRepository.save(target.setDrugs(deduplicated));

        // scheduler
        drugIds.forEach(id -> scheduleService.setIsEnabled(id, false));
    }

    private DrugGroupDTO transformDTO(DrugGroup group) {
        List<DrugDTO> drugs = group.getDrugs().stream().map(DrugDTOMapper::toDTO).toList();
        List<ScheduleTime> scheduleTimes = scheduleService.get(group.getId()).stream().map(s ->
                new ScheduleTime(s.getScheduleTime().toLocalTime(), s.getIsEnabled())
        ).toList();

        return new DrugGroupDTO(
                group.getId(),
                userContext.getUserId(),
                group.getGroupName(),
                scheduleTimes,
                drugs
        );
    }
}
