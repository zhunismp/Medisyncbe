package com.mahidol.drugapi.history.models;

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
public class DrugHistory {
    private UUID drugId;
    private String drugGenericName;
    private String dosageForm;
    private String strength;
    private String unit;
    private Double dose;
    private Double initialAmount;
    private Double remainingAmount;
    private List<DrugHistoryEntry> histories;
    private DrugHistoryStat stats;
    private List<Integer> graphs;
}
