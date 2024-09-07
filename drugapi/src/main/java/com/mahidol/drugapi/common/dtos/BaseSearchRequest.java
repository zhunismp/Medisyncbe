package com.mahidol.drugapi.common.dtos;

import com.mahidol.drugapi.common.models.Pagination;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class BaseSearchRequest {
    // TODO: Extract this information to Context object.
    @NotNull(message = "user id should not be null")
    protected UUID userId;

    protected Pagination pagination;

    public Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

}
