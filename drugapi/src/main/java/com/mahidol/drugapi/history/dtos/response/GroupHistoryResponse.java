package com.mahidol.drugapi.history.dtos.response;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.history.models.GroupHistoryEntry;
import com.mahidol.drugapi.history.models.GroupHistoryStat;
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
public class GroupHistoryResponse {
    private UUID groupId;
    private String groupName;
    private List<ScheduleTime> scheduleTimes;
    private List<UUID> drugIds;
    private List<GroupHistoryEntry> histories;
    private GroupHistoryStat stats;
    private List<Integer> graph;
}
