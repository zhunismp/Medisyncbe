package com.mahidol.drugapi.drug.dtos;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.druggroup.dtos.DrugGroupDTOMapper;

import java.util.List;

public class DrugDTOMapper {

    // For Drug
    public static DrugDTO toDTO(Drug d, List<ScheduleTime> scheduleTimes) {
        return new DrugDTO(
                d.getId(),
                null,
                d.getUserId(),
                d.getGenericName(),
                d.getDosageForm(),
                d.getUnit(),
                d.getStrength(),
                d.getAmount(),
                d.getDose(),
                d.getTakenAmount(),
                d.getUsageTime(),
                scheduleTimes,
                d.getGroups().stream().map(DrugGroupDTOMapper::toDTO).toList(),
                d.getIsInternalDrug(),
                d.getIsArchived()
        );
    }

    // FOr other entities
    public static DrugDTO toDTOWithGroup(Drug d) {
        return new DrugDTO()
                .setId(d.getId())
                .setUserId(d.getUserId())
                .setGenericName(d.getGenericName())
                .setDosageForm(d.getDosageForm())
                .setUnit(d.getUnit())
                .setStrength(d.getStrength())
                .setAmount(d.getAmount())
                .setDose(d.getDose())
                .setTakenAmount(d.getTakenAmount())
                .setGroups(d.getGroups().stream().map(DrugGroupDTOMapper::toDTO).toList())
                .setUsageTime(d.getUsageTime())
                .setIsInternalDrug(d.getIsInternalDrug());
    }

    // For Drug group
    public static DrugDTO toDTO(Drug d) {
        return new DrugDTO()
                .setId(d.getId())
                .setUserId(d.getUserId())
                .setGenericName(d.getGenericName())
                .setDosageForm(d.getDosageForm())
                .setUnit(d.getUnit())
                .setStrength(d.getStrength())
                .setAmount(d.getAmount())
                .setDose(d.getDose())
                .setTakenAmount(d.getTakenAmount())
                .setUsageTime(d.getUsageTime())
                .setIsInternalDrug(d.getIsInternalDrug());
    }
}
