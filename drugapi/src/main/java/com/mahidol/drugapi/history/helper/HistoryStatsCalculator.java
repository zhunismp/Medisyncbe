package com.mahidol.drugapi.history.helper;

import com.mahidol.drugapi.history.models.DrugHistoryStat;
import com.mahidol.drugapi.history.models.GroupHistoryStat;
import com.mahidol.drugapi.history.models.entities.History;
import com.mahidol.drugapi.history.models.types.TakenStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryStatsCalculator {
    public static DrugHistoryStat calculateDrugHistories(List<History> histories) {
        Map<TakenStatus, Long> statusCount = histories.stream()
                .collect(Collectors.groupingBy(History::getStatus, Collectors.counting()));

        int total = histories.size();
        int taken = statusCount.getOrDefault(TakenStatus.TAKEN, 0L).intValue();
        int missed = statusCount.getOrDefault(TakenStatus.MISSED, 0L).intValue();
        int skipped = statusCount.getOrDefault(TakenStatus.SKIPPED, 0L).intValue();
        double takenPercentage = (total != 0) ? (double) taken / total : 0.0;

        return new DrugHistoryStat(taken, skipped, missed, total, takenPercentage);
    }

    public static GroupHistoryStat calculateDrugGroupHistories(List<History> histories) {
        Map<LocalDateTime, List<History>> groupedByDateTime = histories.stream()
                .collect(Collectors.groupingBy(History::getNotifiedAt));



        return null;
    }

    public static List<Integer> generateGraphs() {
        return List.of();
    }
}
