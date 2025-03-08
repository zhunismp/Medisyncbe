package com.mahidol.drugapi.drug.dtos;

import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.models.entites.Drug;

public class DrugDTOMapper {

    /*
     *  return DTO without schedule
     */
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
