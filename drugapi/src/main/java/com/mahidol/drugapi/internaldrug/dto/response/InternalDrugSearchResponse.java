package com.mahidol.drugapi.internaldrug.dto.response;

import com.mahidol.drugapi.internaldrug.models.InternalDrug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InternalDrugSearchResponse {
    private List<InternalDrug> data;
    private int total;
}
