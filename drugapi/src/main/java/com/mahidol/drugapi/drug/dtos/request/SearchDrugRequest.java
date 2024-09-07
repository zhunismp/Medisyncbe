package com.mahidol.drugapi.drug.dtos.request;

import com.mahidol.drugapi.common.dtos.BaseSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchDrugRequest extends BaseSearchRequest {
    private String genericName;

    // Getter for Optional fields
    public Optional<String> getGenericName() {
        return Optional.ofNullable(genericName);
    }

}
