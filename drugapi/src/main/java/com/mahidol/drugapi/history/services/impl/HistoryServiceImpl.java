package com.mahidol.drugapi.history.services.impl;

import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.services.DrugGroupService;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.SearchHistoryResponse;
import com.mahidol.drugapi.history.repositories.HistoryRepository;
import com.mahidol.drugapi.history.services.HistoryService;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final DrugService drugService;
    private final DrugGroupService drugGroupService;

    public HistoryServiceImpl(
            HistoryRepository historyRepository,
            DrugService drugService,
            DrugGroupService drugGroupService
    ) {
        this.historyRepository = historyRepository;
        this.drugService = drugService;
        this.drugGroupService = drugGroupService;
    }

    @Override
    public SearchHistoryResponse search(SearchHistoryRequest request) {
        return null;
    }
}
