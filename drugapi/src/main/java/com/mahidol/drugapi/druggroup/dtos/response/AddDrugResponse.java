package com.mahidol.drugapi.druggroup.dtos.response;

import com.mahidol.drugapi.common.dtos.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDrugResponse extends BaseResponse<AddDrugResponse> {
    private List<DrugGroupWithDrugInfo> data;
    private int total;
    private boolean isCompleted;
}
