package com.mahidol.drugapi.history.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GroupHistoryStat {
    private Integer takenAmount; // each time need to be greater than 60% to consider group taken.
    private Integer totalAmount;
    private Double takenPercent;
}
