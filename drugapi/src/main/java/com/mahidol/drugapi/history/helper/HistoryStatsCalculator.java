package com.mahidol.drugapi.history.helper;

import com.mahidol.drugapi.history.models.DrugHistoryEntry;
import com.mahidol.drugapi.history.models.DrugHistoryStat;
import com.mahidol.drugapi.history.models.GroupHistoryEntry;
import com.mahidol.drugapi.history.models.GroupHistoryStat;
import com.mahidol.drugapi.history.models.types.GroupTakenStatus;
import com.mahidol.drugapi.history.models.types.TakenStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryStatsCalculator {
    public static DrugHistoryStat calculateDrugHistories(List<DrugHistoryEntry> histories) {
        Map<TakenStatus, Long> statusCount = histories.stream()
                .collect(Collectors.groupingBy(DrugHistoryEntry::getStatus, Collectors.counting()));

        int total = histories.size();
        int taken = statusCount.getOrDefault(TakenStatus.TAKEN, 0L).intValue();
        int missed = statusCount.getOrDefault(TakenStatus.MISSED, 0L).intValue();
        int skipped = statusCount.getOrDefault(TakenStatus.SKIPPED, 0L).intValue();
        double takenPercentage = (total != 0) ? (double) taken * 100 / total : 0.0;

        return new DrugHistoryStat(taken, skipped, missed, total, takenPercentage);
    }

    public static GroupHistoryStat calculateDrugGroupHistories(List<GroupHistoryEntry> histories) {
        Map<GroupTakenStatus, Long> statusCount = histories.stream()
                .collect(Collectors.groupingBy(GroupHistoryEntry::getStatus, Collectors.counting()));
        int total = histories.size();
        int allTaken = statusCount.getOrDefault(GroupTakenStatus.ALL_TAKEN, 0L).intValue();
        int partiallyTaken = statusCount.getOrDefault(GroupTakenStatus.PARTIALLY_TAKEN, 0L).intValue();
        int missed = statusCount.getOrDefault(GroupTakenStatus.MISSED, 0L).intValue();
        double takenPercentage = (total != 0) ? (double) (allTaken + partiallyTaken / 2) * 100 / total : 0.0;

        return new GroupHistoryStat(total, allTaken, partiallyTaken, missed, takenPercentage);
    }

    public static List<Integer> generateDrugGraph(List<DrugHistoryEntry> histories) {
        List<DrugHistoryEntry> sorted = histories.stream().sorted(Comparator.comparing(DrugHistoryEntry::getDatetime)).toList();
        List<Integer> result = new ArrayList<>();

        int accumulative = 0;
        int total = 0;
        for (DrugHistoryEntry h: sorted) {
            if (h.getStatus() == TakenStatus.TAKEN) accumulative++;
            total++;

            int p = (accumulative * 100) / total;
            result.add(p);
        }

        return result;
    }

    public static List<Integer> generateGroupGraph(List<GroupHistoryEntry> histories) {
        List<GroupHistoryEntry> sorted = histories.stream().sorted(Comparator.comparing(GroupHistoryEntry::getDatetime)).toList();
        List<Integer> result = new ArrayList<>();

        int accumulative = 0;
        int total = 0;
        for (GroupHistoryEntry h: sorted) {
            if (h.getStatus() == GroupTakenStatus.ALL_TAKEN) accumulative += 2;
            else if (h.getStatus() == GroupTakenStatus.PARTIALLY_TAKEN) accumulative += 1;
            total += 2;

            int p = (accumulative * 100) / total;
            result.add(p);
        }

        return result;
    }
}
