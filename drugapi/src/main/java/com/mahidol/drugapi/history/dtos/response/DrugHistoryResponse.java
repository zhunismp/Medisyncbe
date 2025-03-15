package com.mahidol.drugapi.history.dtos.response;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.history.models.DrugHistoryEntry;
import com.mahidol.drugapi.history.models.DrugHistoryStat;
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
public class DrugHistoryResponse {
    private UUID drugId;
    private String drugGenericName;
    private String dosageForm;
    private String strength;
    private String unit;
    private Double dose;
    private List<ScheduleTime> scheduleTimes;
    private List<DrugHistoryEntry> histories;
    private DrugHistoryStat stats;
    private List<Integer> graphs;
}
