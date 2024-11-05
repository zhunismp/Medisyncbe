package com.mahidol.drugapi.drug.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchDrugResponse {
    private List<DrugDTO> data;
    private int total;
}
