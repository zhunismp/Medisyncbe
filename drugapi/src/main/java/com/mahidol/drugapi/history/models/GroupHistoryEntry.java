package com.mahidol.drugapi.history.models;

import com.mahidol.drugapi.history.models.types.GroupTakenStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GroupHistoryEntry {

    private GroupTakenStatus status;

    private LocalDateTime datetime;

    // If and only if preferred date was set
    private List<DrugHistoryEntry> drugs;

    private Integer takenAmount;
}
