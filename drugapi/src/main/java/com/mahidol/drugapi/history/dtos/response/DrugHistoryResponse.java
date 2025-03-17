package com.mahidol.drugapi.history.dtos.response;

import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.history.models.DrugHistoryEntry;
import com.mahidol.drugapi.history.models.DrugHistoryStat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DrugHistoryResponse {
    private DrugDTO drug;
    private List<DrugHistoryEntry> histories;
    private DrugHistoryStat stats;
    private List<Integer> graphs;
}
