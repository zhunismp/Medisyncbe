package com.mahidol.drugapi.drug.dtos.response;

import com.mahidol.drugapi.common.dtos.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddDrugResponse extends BaseResponse<AddDrugResponse> {
    private List<UUID> data;
    private int total;
    private boolean isCompleted;
}
