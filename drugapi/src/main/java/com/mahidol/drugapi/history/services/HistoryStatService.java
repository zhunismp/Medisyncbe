package com.mahidol.drugapi.history.services;

import com.mahidol.drugapi.history.models.DrugHistoryStat;
import com.mahidol.drugapi.history.models.GroupHistoryStat;
import com.mahidol.drugapi.history.models.entities.History;

import java.util.List;

public interface HistoryStatService {
    DrugHistoryStat calculateDrugHistories(List<History> histories);
    GroupHistoryStat calculateDrugGroupHistories(List<History> histories);
}
