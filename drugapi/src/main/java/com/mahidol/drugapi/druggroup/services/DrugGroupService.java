package com.mahidol.drugapi.druggroup.services;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.services.PaginationService;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.dtos.request.AddDrugRequest;
import com.mahidol.drugapi.druggroup.dtos.request.CreateGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.request.SearchGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.response.AddDrugResponse;
import com.mahidol.drugapi.druggroup.dtos.response.CreateGroupResponse;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupWithDrugInfo;
import com.mahidol.drugapi.druggroup.dtos.response.SearchGroupResponse;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.druggroup.repositories.DrugGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class DrugGroupService {
    private final DrugGroupRepository drugGroupRepository;
    private final DrugService drugService;
    private final PaginationService<DrugGroupWithDrugInfo> paginationService;

    public DrugGroupService(
            DrugGroupRepository drugGroupRepository,
            DrugService drugService,
            PaginationService<DrugGroupWithDrugInfo> paginationService) {
        this.drugGroupRepository = drugGroupRepository;
        this.drugService = drugService;
        this.paginationService = paginationService;
    }

    public CreateGroupResponse create(CreateGroupRequest request) {
        boolean groupNameExists = drugGroupRepository.findByUserId(request.getUserId())
                .stream()
                .map(DrugGroup::getGroupName)
                .anyMatch(name -> name.equalsIgnoreCase(request.getGroupName()));

        if (groupNameExists)
            throw new IllegalArgumentException("Group name already exists, please use another name");

        DrugGroup group = new DrugGroup()
                .setUserId(request.getUserId())
                .setGroupName(request.getGroupName())
                .setSchedules(request.getSchedules())
                .setDrugs(request.getDrugs())
                .setIsEnabled(request.getIsEnabled());

        return new CreateGroupResponse(
                List.of(DrugGroupWithDrugInfo.fromDrugGroup(
                        drugGroupRepository.save(group),
                        drugIds -> drugService.updateAllDrugs(request.getUserId(), drugService.searchAllDrugByDrugsId(request.getUserId(), drugIds).stream()
                                .map(drug -> drug.setIsEnable(false)).toList())
                )),
                1,
                true
        );
    }

    public SearchGroupResponse search(SearchGroupRequest request) {
        List<DrugGroup> drugGroups = drugGroupRepository.findByUserId(request.getUserId());
        List<DrugGroupWithDrugInfo> drugGroupWithDrugInfos = drugGroups.stream()
                .map(drugGroup -> DrugGroupWithDrugInfo.fromDrugGroup(drugGroup, drugsId -> drugService.searchAllDrugByDrugsId(request.getUserId(), drugsId)))
                .toList();

        if (request.getGenericName().isEmpty() && request.getGroupName().isEmpty())
            return new SearchGroupResponse(
                    applyPaginate(drugGroupWithDrugInfos, request.getPagination()),
                    drugGroups.size(),
                    true
            );
        else {
            List<DrugGroupWithDrugInfo> response = drugGroupWithDrugInfos.stream()
                    .filter(drugGroup -> request.getGroupName()
                            .map(groupName -> drugGroup.getGroupName().toLowerCase().contains(groupName.toLowerCase()))
                            .orElse(true))
                    .filter(drugGroup -> request.getGenericName()
                            .map(genericName -> drugGroup.getDrugs().stream().anyMatch(drug -> drug.getGenericName().toLowerCase().contains(genericName.toLowerCase())))
                            .orElse(true)
                    )
                    .toList();

            return new SearchGroupResponse(
                    applyPaginate(response, request.getPagination()),
                    response.size(),
                    true
            );
        }
    }

    public void remove(UUID drugGroupId, UUID userId, Boolean isRemoveDrug) {
        List<UUID> drugIds = findDrugGroup(userId, drugGroupId).getDrugs();

        if (isRemoveDrug)
            drugService.deleteAllByDrugIds(userId, drugIds);
        else
            // After remove drug from the drug group, we need to set isEnabled to true again
            // This is for make old notification of drug behavior the same as before.
            drugService.updateAllDrugs(userId, drugService.searchAllDrugByDrugsId(userId, drugIds).stream()
                    .map(drug -> drug.setIsEnable(true)).toList());


        drugGroupRepository.deleteById(drugGroupId);
    }

    public AddDrugResponse addDrugsToGroup(AddDrugRequest request) {
        DrugGroup drugGroup = findDrugGroup(request.getUserId(), request.getGroupId());
        List<Drug> drugs = drugService.searchAllDrugByDrugsId(request.getUserId(), request.getDrugs()).stream().map(drug -> drug.setIsEnable(false)).toList();

        DrugGroupWithDrugInfo response = DrugGroupWithDrugInfo.fromDrugGroup(
                drugGroupRepository.save(drugGroup.setDrugs(Stream.concat(drugGroup.getDrugs().stream(), drugs.stream().map(Drug::getId)).distinct().toList())),
                drugIds -> Stream.concat(
                        drugService.updateAllDrugs(request.getUserId(), drugs).stream(),
                        drugService.searchAllDrugByDrugsId(request.getUserId(), drugIds).stream()
                ).distinct().toList()
        );

        return new AddDrugResponse(
                List.of(response),
                1,
                true
        );
    }

    private DrugGroup findDrugGroup(UUID userId, UUID groupId) {
        return drugGroupRepository.findById(groupId)
                .map(dg -> {
                    if (!dg.getUserId().equals(userId))
                        throw new IllegalArgumentException("User may not own that drug group");
                    else return dg;
                })
                .orElseThrow(() -> new EntityNotFoundException("User try to add some drug to non exists drug group"));
    }

    private List<DrugGroupWithDrugInfo> applyPaginate(List<DrugGroupWithDrugInfo> drugGroups, Optional<Pagination> pagination) {
        return pagination.map(p -> paginationService.paginate(drugGroups, p)).orElse(drugGroups);

    }
}
