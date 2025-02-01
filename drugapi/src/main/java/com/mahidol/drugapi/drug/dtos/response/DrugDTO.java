package com.mahidol.drugapi.drug.dtos.response;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.models.type.MealCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DrugDTO {
    private UUID id;

    private String drugImageUrl;

    private UUID userId;

    private String genericName;

    private String dosageForm;

    private String unit;

    private String strength;

    private double amount;

    private double dose;

    private double takenAmount;

    private MealCondition usageTime;

    private List<ScheduleTime> scheduleTimes;

    private Boolean isInternalDrug = false;

    public static DrugDTO fromDrug(Drug drug, Optional<String> drugImageUrl) {
        return new DrugDTO()
                .setId(drug.getId())
                .setDrugImageUrl(drugImageUrl.orElse(""))
                .setUserId(drug.getUserId())
                .setGenericName(drug.getGenericName())
                .setDosageForm(drug.getDosageForm())
                .setUnit(drug.getUnit())
                .setStrength(drug.getStrength())
                .setAmount(drug.getAmount())
                .setDose(drug.getDose())
                .setTakenAmount(drug.getTakenAmount())
                .setUsageTime(drug.getUsageTime())
                .setIsInternalDrug(drug.getIsInternalDrug());
    }
}
