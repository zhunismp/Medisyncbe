package com.mahidol.drugapi.druggroup.dtos.request;

import com.mahidol.drugapi.common.models.Pagination;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchGroupRequest {

    private UUID relativeId;
    private Pagination pagination;

    public Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

    public Optional<UUID> getRelativeId() {
        return Optional.ofNullable(relativeId);
    }
}
