package com.mahidol.drugapi.history.dtos.request;

import com.mahidol.drugapi.history.models.types.TakenStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class HistoryEntry {
    private UUID id;
    private TakenStatus status;
}
