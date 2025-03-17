package com.mahidol.drugapi.history.services;

import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.DrugHistoryResponse;
import com.mahidol.drugapi.history.dtos.response.GroupHistoryResponse;
import com.mahidol.drugapi.history.dtos.response.NotiHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HistoryService {
    GroupHistoryResponse searchGroupHistory(SearchHistoryRequest request);
    DrugHistoryResponse searchDrugHistory(SearchHistoryRequest request);
    NotiHistoryResponse searchNotiHistory(LocalDate preferredDate);
    void editHistory(EditHistoryRequest request);
    void removeHistoriesByDrugIds(List<UUID> drugIds);
}
