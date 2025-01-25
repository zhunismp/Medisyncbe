package com.mahidol.drugapi.history.services;

import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.SearchHistoryResponse;

public interface HistoryService {
    SearchHistoryResponse search(SearchHistoryRequest request);
}
