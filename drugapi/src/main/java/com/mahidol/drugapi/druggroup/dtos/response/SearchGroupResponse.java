package com.mahidol.drugapi.druggroup.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchGroupResponse {
    private List<DrugGroupDTO> data;
    private int total;
}
