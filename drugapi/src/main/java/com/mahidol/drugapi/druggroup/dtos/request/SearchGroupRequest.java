package com.mahidol.drugapi.druggroup.dtos.request;

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
public class SearchGroupRequest {
    @NotNull(message = "user id should not be null")
    private UUID userId;
    private Pagination pagination;
    private String groupName;
    private String genericName;

    public Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

    public Optional<String> getGroupName() {
        return Optional.ofNullable(groupName);
    }

    public Optional<String> getGenericName() {
        return Optional.ofNullable(genericName);
    }
}
