package com.mahidol.drugapi.history.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class History {
    private UUID id;
    private String status;
    private LocalDateTime notifiedAt;

    public static History fromH(com.mahidol.drugapi.history.models.entities.History h) {
        return new History(
                h.getId(),
                h.getStatus().toString(),
                h.getNotifiedAt()
        );
    }
}
