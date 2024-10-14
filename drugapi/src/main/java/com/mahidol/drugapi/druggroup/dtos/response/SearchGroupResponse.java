package com.mahidol.drugapi.druggroup.dtos.response;

import com.mahidol.drugapi.druggroup.entities.DrugGroupWithDrugInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchGroupResponse {
    private List<DrugGroupWithDrugInfo> data;
    private int total;
}
