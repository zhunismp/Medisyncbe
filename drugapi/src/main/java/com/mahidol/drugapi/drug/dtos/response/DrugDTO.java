package com.mahidol.drugapi.drug.dtos.response;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.models.type.MealCondition;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
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

    private List<DrugGroupDTO> groups;

    private Boolean isInternalDrug = false;
}
