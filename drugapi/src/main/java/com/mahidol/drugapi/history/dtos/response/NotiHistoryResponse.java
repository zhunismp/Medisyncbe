package com.mahidol.drugapi.history.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NotiHistoryResponse {
    private List<GroupHistoryResponse> groups;
    private List<DrugHistoryResponse> drugs;
    private Integer total;
}
