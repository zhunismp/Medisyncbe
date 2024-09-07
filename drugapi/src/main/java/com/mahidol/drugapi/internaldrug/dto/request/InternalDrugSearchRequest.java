package com.mahidol.drugapi.internaldrug.dto.request;

import com.mahidol.drugapi.common.dtos.BaseSearchRequest;
import com.mahidol.drugapi.common.utils.JsonParser;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InternalDrugSearchRequest extends BaseSearchRequest {
    @NotBlank(message = "generic name should not be null")
    private String genericName;


    @Override
    public String toString() {
        return JsonParser.toJsonString(this)
                .getOrElse("");
    }
}
