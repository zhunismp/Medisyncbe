package com.mahidol.drugapi.history.dtos.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EditHistoryRequest {
    private List<HistoryEntry> histories;
}
