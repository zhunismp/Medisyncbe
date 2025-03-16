package com.mahidol.drugapi.druggroup.dtos;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.dtos.DrugDTOMapper;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;

import java.util.List;

public class DrugGroupDTOMapper {

    // For Drug group
    public static DrugGroupDTO toDTO(DrugGroup group, List<ScheduleTime> scheduleTimes) {
        return new DrugGroupDTO(
                group.getId(),
                group.getUserId(),
                group.getGroupName(),
                scheduleTimes,
                group.getDrugs().stream().map(DrugDTOMapper::toDTO).toList()
        );
    }

    // For Drug
    public static DrugGroupDTO toDTO(DrugGroup group) {
        return new DrugGroupDTO()
                .setId(group.getId())
                .setUserId(group.getUserId())
                .setGroupName(group.getGroupName());
    }
}
