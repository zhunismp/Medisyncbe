package com.mahidol.drugapi.history.services.impl;

import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.services.DrugGroupService;
import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.HistoryEntry;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.DrugHistoryResponse;
import com.mahidol.drugapi.history.helper.HistoryStatsCalculator;
import com.mahidol.drugapi.history.dtos.response.GroupHistoryResponse;
import com.mahidol.drugapi.history.models.DrugHistoryEntry;
import com.mahidol.drugapi.history.models.GroupHistoryEntry;
import com.mahidol.drugapi.history.models.entities.History;
import com.mahidol.drugapi.history.models.types.GroupTakenStatus;
import com.mahidol.drugapi.history.models.types.TakenStatus;
import com.mahidol.drugapi.history.repositories.HistoryRepository;
import com.mahidol.drugapi.history.services.HistoryService;
import com.mahidol.drugapi.relation.services.RelationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final DrugService drugService;
    private final DrugGroupService drugGroupService;
    private final RelationService relationService;
    private final UserContext userContext;

    public HistoryServiceImpl(
            HistoryRepository historyRepository,
            DrugService drugService,
            DrugGroupService drugGroupService,
            RelationService relationService,
            UserContext userContext
    ) {
        this.historyRepository = historyRepository;
        this.drugService = drugService;
        this.drugGroupService = drugGroupService;
        this.relationService = relationService;
        this.userContext = userContext;
    }

    @Override
    public GroupHistoryResponse searchGroupHistory(SearchHistoryRequest request) {
        UUID userId = request.getRelativeId().map(i -> {
            if (!relationService.getIncomingPermission(i).getReadable())
                throw new IllegalArgumentException("Access denied from your friend");

            return i;
        }).orElse(userContext.getUserId());

        // safe get here, since controller already validated.
        return drugGroupService.getDrugGroupByGroupIdOpt(userId, request.getGroupId().get()).map(g -> {
            List<History> rawHistories = getRawHistories(userId, request.getPreferredDate(), request.getYear(), request.getMonth());
            List<GroupHistoryEntry> histories = buildGroupHistories(rawHistories);

            return new GroupHistoryResponse(
                    g.getId(),
                    g.getGroupName(),
                    g.getDrugs().stream().map(Drug::getId).toList(),
                    histories,
                    HistoryStatsCalculator.calculateDrugGroupHistories(histories),
                    HistoryStatsCalculator.generateGraphs()
            );
        }).orElseThrow(() -> new IllegalArgumentException("User might not own this group or group not exists"));
    }

    @Override
    public DrugHistoryResponse searchDrugHistory(SearchHistoryRequest request) {
        UUID userId = request.getRelativeId().map(i -> {
            if (!relationService.getIncomingPermission(i).getReadable())
                throw new IllegalArgumentException("Access denied from your friend");

            return i;
        }).orElse(userContext.getUserId());

        // safe get here, since controller already validated.
        return drugService.searchDrugByDrugId(userId, request.getDrugId().get()).map(drug -> {
            List<History> rawHistories = getRawHistories(userId, request.getPreferredDate(), request.getYear(), request.getMonth());
            List<DrugHistoryEntry> histories = rawHistories.stream()
                    .filter(h -> h.getDrugId().equals(drug.getId()))
                    .map(DrugHistoryEntry::fromH)
                    .toList();

            return new DrugHistoryResponse(
                    drug.getId(),
                    drug.getGenericName(),
                    drug.getDosageForm(),
                    drug.getStrength(),
                    drug.getUnit(),
                    drug.getDose(),
                    histories,
                    HistoryStatsCalculator.calculateDrugHistories(histories),
                    HistoryStatsCalculator.generateGraphs()
            );
        }).orElseThrow(() -> new IllegalArgumentException("User might not own this group or group not exists"));
    }


    @Override
    public void editHistory(EditHistoryRequest request) {
        UUID userId = userContext.getUserId();

        List<UUID> historyIds = request.getHistories()
                .stream()
                .map(HistoryEntry::getId)
                .toList();

        if (!validate(userId, historyIds)) {
            throw new IllegalArgumentException("User might not own these histories");
        }

        List<History> histories = historyRepository.findAllById(historyIds);
        List<Drug> updateDrugs = drugService.searchAllDrugByDrugsId(userId,  histories.stream()
                .filter(history -> request.getHistories().stream()
                        .anyMatch(h -> h.getStatus() == TakenStatus.TAKEN)
                )
                .map(History::getDrugId).toList()
        );

        // set status
        histories.forEach(history ->
            request.getHistories().stream()
                    .filter(h -> h.getId().equals(history.getId()))
                    .findFirst()
                    .ifPresent(h -> history.setStatus(h.getStatus()))
        );

        // update taken amount
        // TODO: Archive drug that taken amt >= amt
        updateDrugs.forEach(drug -> drug.setTakenAmount(drug.getTakenAmount() + drug.getDose()));

        historyRepository.saveAll(histories);
        drugService.saveAllDrugs(userId, updateDrugs);
    }

    @Override
    public void removeHistoriesByDrugIds(List<UUID> drugIds) {
        historyRepository.deleteAllByDrugIds(drugIds);
    }

//
//    private <T> List<T> paginate(List<T> items, int pageNumber, int pageSize) {
//        int start = (pageNumber - 1) * pageSize;
//        int end = Math.min(start + pageSize, items.size());
//
//        return items.subList(start, end);
//    }

    private boolean validate(UUID userId, List<UUID> historyIds) {
        List<UUID> validHistories = historyRepository.findByUserId(userId).stream().map(History::getId).toList();

        return new HashSet<>(validHistories).containsAll(historyIds);
    }

    private List<History> getRawHistories(UUID userId, Optional<Integer> preferredDate, Integer year, Integer month) {
        return preferredDate
                .map(date -> historyRepository.findByUserIdAndDate(userId, LocalDate.of(year, month, date)))
                .orElseGet(() -> historyRepository.findByUserIdAndMonthAndYear(userId, month, year));
    }

    private List<GroupHistoryEntry> buildGroupHistories(List<History> histories) {
        return histories.stream()
                .collect(Collectors.groupingBy(History::getNotifiedAt))
                .entrySet().stream()
                .map(entry -> transformGroupEntry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private GroupHistoryEntry transformGroupEntry(LocalDateTime dt, List<History> histories) {
        int takenAmt = (int) histories.stream().filter(h -> h.getStatus() == TakenStatus.TAKEN).count();
        int total = histories.size();
        int takenPercentage = (total == 0) ? 0 : (takenAmt * 100 / total);
        GroupTakenStatus status = (takenPercentage == 100) ? GroupTakenStatus.ALL_TAKEN
                : (takenPercentage > 50) ? GroupTakenStatus.PARTIALLY_TAKEN
                : GroupTakenStatus.MISSED;

        return new GroupHistoryEntry(status, dt, takenAmt);
    }
}
