package com.mahidol.drugapi.history.models;

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
    private UUID id;
    private String status;
    private LocalDateTime notifiedAt;
    private Integer takenAmount;

    public static GroupHistoryEntry fromH(com.mahidol.drugapi.history.models.entities.History h) {
        return new GroupHistoryEntry(
                h.getId(),
                h.getStatus().toString(),
                h.getNotifiedAt(),
                null
        );
    }
}
