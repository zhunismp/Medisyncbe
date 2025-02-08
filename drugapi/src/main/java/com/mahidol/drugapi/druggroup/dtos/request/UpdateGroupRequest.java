package com.mahidol.drugapi.druggroup.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mahidol.drugapi.common.models.ScheduleTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateGroupRequest {
    private UUID groupId;

    private String groupName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<ScheduleTime> scheduleTimes;

    public Optional<String> getGroupName() {
        return Optional.ofNullable(groupName);
    }

    public Optional<List<ScheduleTime>> getScheduleTimes() {
        return Optional.ofNullable(scheduleTimes);
    }
}
