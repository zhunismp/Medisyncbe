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
    private Integer total;
    private Integer allTaken;
    private Integer partiallyTaken;
    private Integer missed;
    private Double takenPercent;
}
