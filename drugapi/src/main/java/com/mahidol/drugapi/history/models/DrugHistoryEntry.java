package com.mahidol.drugapi.history.models;

import com.mahidol.drugapi.history.models.entities.History;
import com.mahidol.drugapi.history.models.types.TakenStatus;
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
public class DrugHistoryEntry {
    private UUID id;
    private TakenStatus status;
    private LocalDateTime notifiedAt;

    public static DrugHistoryEntry fromH(History h) {
        return new DrugHistoryEntry(
                h.getId(),
                h.getStatus(),
                h.getNotifiedAt()
        );
    }
}
