package com.mahidol.drugapi.internaldrug.dto.request;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.common.utils.JsonParser;
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
public class InternalDrugSearchRequest {

    private Pagination pagination;
    @NotBlank(message = "generic name should not be null")
    private String genericName;


    public Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

    @Override
    public String toString() {
        return JsonParser.toJsonString(this)
                .getOrElse("");
    }
}
