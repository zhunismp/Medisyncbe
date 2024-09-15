package com.mahidol.drugapi.drug.dtos.response;

import com.mahidol.drugapi.common.dtos.BaseResponse;
import com.mahidol.drugapi.drug.models.entites.Drug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateDrugResponse extends BaseResponse<CreateDrugResponse> {
    private List<Drug> data;
    private int total;
    private boolean isCompleted;
}
