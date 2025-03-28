package com.mahidol.drugapi.druggroup.dtos.response;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DrugGroupDTO {
    private UUID id;

    private UUID userId;

    private String groupName;

    private Boolean isArchived;

    private List<ScheduleTime> scheduleTimes;

    private List<DrugDTO> drugs;
}
