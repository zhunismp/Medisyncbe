package com.mahidol.drugapi.history.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DrugHistoryStat {
    private Integer takenAmount;
    private Integer skippedAmount;
    private Integer missingAmount;
    private Integer totalAmount;
    private Double takenPercent;
}
