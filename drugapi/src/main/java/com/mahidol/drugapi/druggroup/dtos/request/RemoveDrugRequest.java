package com.mahidol.drugapi.druggroup.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemoveDrugRequest {
    @NotNull(message = "group id should not be null")
    private UUID groupId;

    @NotEmpty(message = "drug list should not be empty")
    private List<UUID> drugs;

    private Boolean isRemoveDrug;
}
