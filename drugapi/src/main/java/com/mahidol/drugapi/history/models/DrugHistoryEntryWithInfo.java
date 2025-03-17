package com.mahidol.drugapi.history.models;

import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.history.models.types.TakenStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DrugHistoryEntryWithInfo {
    private DrugDTO info;
    private TakenStatus status;
    private LocalDateTime datetime;
}
