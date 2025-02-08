package com.mahidol.drugapi.history.services;

import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.SearchHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface HistoryService {
    SearchHistoryResponse search(SearchHistoryRequest request);
    void editHistory(EditHistoryRequest request);
    void removeHistoriesByDrugIds(List<UUID> drugIds);
}
