package com.mahidol.drugapi.druggroup.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mahidol.drugapi.common.models.Schedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGroupRequest {
    @NotNull(message = "user id should not be null")
    private UUID userId;

    @NotBlank(message = "device token is mandatory")
    private String deviceToken;

    @NotBlank(message = "group name should not be null")
    private String groupName;

    @NotNull(message = "schedules should not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Schedule> schedules;

    @NotNull(message = "drugs should not be null")
    private List<UUID> drugs;

    private Boolean isEnabled;

    public Boolean getIsEnabled() {
        return Optional.ofNullable(isEnabled).orElse(true);
    }
}
