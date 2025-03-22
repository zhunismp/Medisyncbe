package com.mahidol.drugapi.history.services.impl;

import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.dtos.DrugDTOMapper;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.druggroup.dtos.DrugGroupDTOMapper;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.druggroup.services.DrugGroupService;
import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.HistoryEntry;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.DrugHistoryResponse;
import com.mahidol.drugapi.history.dtos.response.NotiHistoryResponse;
import com.mahidol.drugapi.history.helper.HistoryStatsCalculator;
import com.mahidol.drugapi.history.dtos.response.GroupHistoryResponse;
import com.mahidol.drugapi.history.models.DrugHistoryEntry;
import com.mahidol.drugapi.history.models.DrugHistoryEntryWithInfo;
import com.mahidol.drugapi.history.models.GroupHistoryEntry;
import com.mahidol.drugapi.history.models.entities.History;
import com.mahidol.drugapi.history.models.types.GroupTakenStatus;
import com.mahidol.drugapi.history.models.types.TakenStatus;
import com.mahidol.drugapi.history.repositories.HistoryRepository;
import com.mahidol.drugapi.history.services.HistoryService;
import com.mahidol.drugapi.relation.services.RelationService;
import com.mahidol.drugapi.schedule.services.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
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
    private final ScheduleService scheduleService;
    private final UserContext userContext;

    public HistoryServiceImpl(
            HistoryRepository historyRepository,
            DrugService drugService,
            DrugGroupService drugGroupService,
            RelationService relationService,
            ScheduleService scheduleService,
            UserContext userContext
    ) {
        this.historyRepository = historyRepository;
        this.drugService = drugService;
        this.drugGroupService = drugGroupService;
        this.relationService = relationService;
        this.scheduleService = scheduleService;
        this.userContext = userContext;
    }

    @Override
    public GroupHistoryResponse searchGroupHistory(SearchHistoryRequest request) {
        UUID userId = resolveUserId(userContext.getUserId(), request.getRelativeId());

        // safe get here, since controller already validated.
        // TODO: refactor this to optimize query
        return drugGroupService.getDrugGroupByGroupIdOpt(userId, request.getGroupId().get()).map(g -> {
            List<History> rawHistories = getRawHistories(userId, request.getPreferredDate(), request.getYear(), request.getMonth())
                    .stream().filter(h -> h.getGroupId() != null && h.getGroupId().equals(g.getId())).toList();
            List<GroupHistoryEntry> histories = buildGroupHistories(userId, rawHistories, request.getPreferredDate());
            List<ScheduleTime> scheduleTimes = scheduleService.get(g.getId()).stream().map(ScheduleTime::fromSchedule).toList();

            return new GroupHistoryResponse(
                    DrugGroupDTOMapper.toDTO(g, scheduleTimes),
                    histories,
                    HistoryStatsCalculator.calculateDrugGroupHistories(histories),
                    HistoryStatsCalculator.generateGroupGraph(histories)
            );
        }).orElseThrow(() -> new IllegalArgumentException("User might not own this group or group not exists"));
    }

    @Override
    public DrugHistoryResponse searchDrugHistory(SearchHistoryRequest request) {
        UUID userId = resolveUserId(userContext.getUserId(), request.getRelativeId());

        // safe get here, since controller already validated.
        return drugService.searchDrugByDrugId(userId, request.getDrugId().get()).map(drug -> {
            List<History> rawHistories = getRawHistories(userId, request.getPreferredDate(), request.getYear(), request.getMonth())
                    .stream().filter(h -> h.getDrugId().equals(drug.getId())).toList();
            List<DrugHistoryEntry> histories = buildDrugHistories(rawHistories);
            List<ScheduleTime> scheduleTimes = scheduleService.get(drug.getId()).stream().map(ScheduleTime::fromSchedule).toList();

            return new DrugHistoryResponse(
                    DrugDTOMapper.toDTO(drug, scheduleTimes),
                    histories,
                    HistoryStatsCalculator.calculateDrugHistories(histories),
                    HistoryStatsCalculator.generateDrugGraph(histories)
            );
        }).orElseThrow(() -> new IllegalArgumentException("User might not own this group or group not exists"));
    }

    @Override
    public NotiHistoryResponse searchNotiHistory(LocalDate preferredDate) {
        List<Drug> allDrugs = drugService.searchDrugByUserId(userContext.getUserId()).stream().filter(d -> d.getGroups().isEmpty()).toList();
        List<DrugGroup> allGroups = drugGroupService.searchGroupByUserId(userContext.getUserId());
        List<History> rawHistories = historyRepository.findByUserIdAndDate(userContext.getUserId(), preferredDate);

        Map<Boolean, List<History>> partitioned = rawHistories.stream().collect(Collectors.partitioningBy(h -> h.getGroupId() != null));
        List<History> groupHistories = partitioned.get(true);
        List<History> drugHistories = partitioned.get(false);

        List<DrugHistoryResponse> drugHistoryResponses  = allDrugs.stream().map(d -> {
            List<DrugHistoryEntry> histories =
                    buildDrugHistories(drugHistories.stream().filter(h -> h.getDrugId().equals(d.getId())).toList());
            List<ScheduleTime> scheduleTimes = scheduleService.get(d.getId()).stream().map(ScheduleTime::fromSchedule).toList();

            return new DrugHistoryResponse(
                    DrugDTOMapper.toDTO(d, scheduleTimes),
                    histories,
                    null,
                    null
            );
        }).toList();

        List<GroupHistoryResponse> groupHistoryResponses = allGroups.stream().map(g -> {
            List<GroupHistoryEntry> histories =
                    buildGroupHistories(
                            userContext.getUserId(),
                            groupHistories.stream().filter(h -> h.getGroupId().equals(g.getId())).toList(),
                            Optional.of(preferredDate.getDayOfMonth())
                    );
            List<ScheduleTime> scheduleTimes = scheduleService.get(g.getId()).stream().map(ScheduleTime::fromSchedule).toList();

            return new GroupHistoryResponse(
                    DrugGroupDTOMapper.toDTO(g, scheduleTimes),
                    histories,
                    null,
                    null
            );

        }).toList();

        return new NotiHistoryResponse(
            groupHistoryResponses,
            drugHistoryResponses,
            groupHistoryResponses.size() + drugHistoryResponses.size()
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

    private List<DrugHistoryEntryWithInfo> buildDrugHistoriesWithInfo(UUID userId, List<History> histories) {
        List<UUID> drugIds = histories.stream().map(History::getDrugId).distinct().toList();
        Map<UUID, List<DrugDTO>> drugs =  drugService.searchAllDrugByDrugsId(userId, drugIds).stream()
                .map(DrugDTOMapper::toDTO)
                .collect(Collectors.groupingBy(DrugDTO::getId));

        return histories.stream().map(h -> new DrugHistoryEntryWithInfo(
                h.getId(),
                drugs.get(h.getDrugId()).stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Drug not found")),
                h.getStatus(),
                h.getNotifiedAt()
        )).toList();

    }

    private List<DrugHistoryEntry> buildDrugHistories(List<History> histories) {
       return histories.stream()
               .map(DrugHistoryEntry::fromH)
               .sorted(Comparator.comparing(DrugHistoryEntry::getDatetime))
               .toList();
    }

    private List<GroupHistoryEntry> buildGroupHistories(UUID userId, List<History> histories, Optional<Integer> preferredDate) {
        return histories.stream()
                .collect(Collectors.groupingBy(History::getNotifiedAt))
                .entrySet().stream()
                .map(entry -> transformGroupEntry(userId, entry.getKey(), entry.getValue(), preferredDate))
                .sorted(Comparator.comparing(GroupHistoryEntry::getDatetime))
                .collect(Collectors.toList());
    }

    // TODO: Find the better way to hide drug histories
    private GroupHistoryEntry transformGroupEntry(UUID userId, LocalDateTime dt, List<History> histories, Optional<Integer> preferredDate) {
        int takenAmt = (int) histories.stream().filter(h -> h.getStatus() == TakenStatus.TAKEN).count();
        int total = histories.size();
        int takenPercentage = (total == 0) ? 0 : (takenAmt * 100 / total);
        GroupTakenStatus status = (takenPercentage == 100) ? GroupTakenStatus.ALL_TAKEN
                : (takenPercentage > 0) ? GroupTakenStatus.PARTIALLY_TAKEN
                : GroupTakenStatus.MISSED;

        if (preferredDate.isPresent())
            return new GroupHistoryEntry(status, dt, buildDrugHistoriesWithInfo(userId, histories), takenAmt);
        else
            return new GroupHistoryEntry(status, dt, null, takenAmt);
    }

    private UUID resolveUserId(UUID userId, Optional<UUID> relativeId) {
        return relativeId.map(r -> {
            if (!relationService.getIncomingPermission(r).getReadable())
                throw new IllegalArgumentException("Access denied from your friend");

            return r;
        }).orElse(userId);
    }
}
