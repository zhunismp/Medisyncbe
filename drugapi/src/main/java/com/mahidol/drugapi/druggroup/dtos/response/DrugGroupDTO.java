package com.mahidol.drugapi.druggroup.dtos.response;

import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class DrugGroupDTO {
    private UUID id;

    private UUID userId;

    private String groupName;

    private List<LocalTime> schedules;

    private List<Drug> drugs;

    private Boolean isEnabled;

    public static DrugGroupDTO fromDrugGroup(DrugGroup drugGroup, Function<List<UUID>, List<Drug>> populateDrugs) {
        return new DrugGroupDTO(
                drugGroup.getId(),
                drugGroup.getUserId(),
                drugGroup.getGroupName(),
                drugGroup.getSchedules(),
                populateDrugs.apply(drugGroup.getDrugs()),
                drugGroup.getIsEnabled()
        );
    }

}
