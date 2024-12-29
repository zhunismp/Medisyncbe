package com.mahidol.drugapi.drug.dtos.request;

import com.mahidol.drugapi.common.models.Pagination;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class SearchDrugRequest {
    @NotNull(message = "user id should not be null")
    private UUID userId;

    @NotBlank(message = "device token is mandatory")
    private String deviceToken;

    private Pagination pagination;

    private String genericName;


    // Getter for Optional fields
    public Optional<String> getGenericName() {
        return Optional.ofNullable(genericName);
    }

    public Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

}
