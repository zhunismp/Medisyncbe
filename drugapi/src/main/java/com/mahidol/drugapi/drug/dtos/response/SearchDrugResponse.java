package com.mahidol.drugapi.drug.dtos.response;

import com.mahidol.drugapi.drug.models.entites.Drug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchDrugResponse {
    private List<Drug> data;
    private int total;
    private boolean isCompleted;
}
