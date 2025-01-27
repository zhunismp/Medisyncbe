package com.mahidol.drugapi.history.services.impl;

import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.common.models.Pagination;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.services.DrugGroupService;
import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.HistoryEntry;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.SearchHistoryResponse;
import com.mahidol.drugapi.history.models.DrugHistory;
import com.mahidol.drugapi.history.models.GroupHistory;
import com.mahidol.drugapi.history.models.entities.History;
import com.mahidol.drugapi.history.models.types.TakenStatus;
import com.mahidol.drugapi.history.repositories.HistoryRepository;
import com.mahidol.drugapi.history.services.HistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final DrugService drugService;
    private final DrugGroupService drugGroupService;
    private final UserContext userContext;

    public HistoryServiceImpl(
            HistoryRepository historyRepository,
            DrugService drugService,
            DrugGroupService drugGroupService,
            UserContext userContext
    ) {
        this.historyRepository = historyRepository;
        this.drugService = drugService;
        this.drugGroupService = drugGroupService;
        this.userContext = userContext;
    }

    /*
        1. If date specify, search only for matched date
        2. Else search for range of month
     */
    @Override
    public SearchHistoryResponse search(SearchHistoryRequest request) {
        UUID userId = userContext.getUserId();
        List<History> histories = request.getPreferredDate()
                .map(d -> historyRepository.findByUserIdAndDate(userId, LocalDate.of(request.getYear(), request.getMonth(), d)))
                .orElseGet(() -> historyRepository.findByUserIdAndMonth(userId, request.getMonth(), request.getYear()));

        List<History> filteredHistories = request.getReferenceId()
                .map(id -> histories.stream()
                        .filter(x -> id.equals(x.getDrugId()) || id.equals(x.getGroupId()))
                        .toList()
                )
                .orElse(histories);

        Map<Boolean, List<History>> partitioned = histories.stream()
                .collect(Collectors.partitioningBy(history -> history.getGroupId() != null));

        List<DrugHistory> drugHistories = buildDrugHistories(userId, partitioned.get(false));
        List<GroupHistory> groupHistories = buildDrugGroupHistories(userId, partitioned.get(true));

        Pagination pagination = request.getPagination().orElse(null);
        if (pagination != null) {
            int pageNumber = pagination.getNumber();
            int pageSize = pagination.getSize();

            drugHistories = paginate(drugHistories, pageNumber, pageSize);
            groupHistories = paginate(groupHistories, pageNumber, pageSize);
        }

        return new SearchHistoryResponse(
                groupHistories,
                drugHistories,
                pagination
        );
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
                .map(History::getUserId).toList()
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

    private List<DrugHistory> buildDrugHistories(UUID userId, List<History> drugHistories) {
        return drugHistories.stream()
                .collect(Collectors.groupingBy(History::getDrugId))
                .entrySet().stream()
                .flatMap(entry -> drugService.searchDrugByDrugId(userId, entry.getKey()).stream().map(drug -> new DrugHistory(
                        drug.getId(),
                        drug.getGenericName(),
                        drug.getDosageForm(),
                        drug.getUnit(),
                        drug.getAmount(),
                        drug.getAmount() - drug.getTakenAmount(),
                        entry.getValue().stream()
                                .map(com.mahidol.drugapi.history.models.History::fromH)
                                .sorted(Comparator.comparing(com.mahidol.drugapi.history.models.History::getNotifiedAt))
                                .toList()
                )))
                .toList();
    }

    private List<GroupHistory> buildDrugGroupHistories(UUID userId, List<History> drugHistories) {
        return drugHistories.stream()
                .collect(Collectors.groupingBy(History::getGroupId))
                .entrySet().stream()
                .flatMap(entry -> drugGroupService.getDrugGroupByGroupIdOpt(userId, entry.getKey()).stream().map(dg -> new GroupHistory(
                        dg.getId(),
                        dg.getGroupName(),
                        buildDrugHistories(userId, entry.getValue())
                )))
                .toList();
    }

    private <T> List<T> paginate(List<T> items, int pageNumber, int pageSize) {
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, items.size());

        return items.subList(start, end);
    }

    private boolean validate(UUID userId, List<UUID> historyIds) {
        List<UUID> validHistories = historyRepository.findByUserId(userId).stream().map(History::getUserId).toList();

        return new HashSet<>(validHistories).containsAll(historyIds);
    }
}
