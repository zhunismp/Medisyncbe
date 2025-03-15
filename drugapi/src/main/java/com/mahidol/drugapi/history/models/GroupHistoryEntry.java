package com.mahidol.drugapi.history.models;

import com.mahidol.drugapi.history.models.types.GroupTakenStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GroupHistoryEntry {
    private GroupTakenStatus status;
    private LocalDateTime notifiedAt;
    private Integer takenAmount;
}
