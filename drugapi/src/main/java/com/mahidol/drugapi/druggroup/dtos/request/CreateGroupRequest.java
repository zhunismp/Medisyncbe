package com.mahidol.drugapi.druggroup.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mahidol.drugapi.common.models.ScheduleTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGroupRequest {

    @NotBlank(message = "group name should not be null")
    private String groupName;

    @NotNull(message = "schedules should not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<ScheduleTime> scheduleTimes;

    @NotNull(message = "drugs should not be null")
    private List<UUID> drugs;
}
