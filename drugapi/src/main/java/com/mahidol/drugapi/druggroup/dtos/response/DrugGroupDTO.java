package com.mahidol.drugapi.druggroup.dtos.response;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.models.entites.Drug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DrugGroupDTO {
    private UUID id;

    private UUID userId;

    private String groupName;

    private List<ScheduleTime> scheduleTimes;

    private List<Drug> drugs;

//    public static DrugGroupDTO fromDrugGroup(DrugGroup drugGroup, Function<List<UUID>, List<Drug>> populateDrugs) {
//        return  new DrugGroupDTO()
//                .setId(drugGroup.getId())
//                .setUserId(drugGroup.getUserId())
//                .setGroupName(drugGroup.getGroupName())
//                .setDrugs(populateDrugs.apply((drugGroup.getDrugs())));
//    }
}
