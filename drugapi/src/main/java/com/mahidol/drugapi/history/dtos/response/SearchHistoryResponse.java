package com.mahidol.drugapi.history.dtos.response;

import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.history.models.DrugHistory;
import com.mahidol.drugapi.history.models.GroupHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryResponse {
    private List<GroupHistory> groups;
    private List<DrugHistory> drugs;
    private Pagination pagination;
}
