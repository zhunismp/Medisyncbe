package com.mahidol.drugapi.notification.cron.sender;

import com.mahidol.drugapi.common.services.SchedulerService;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.drug.services.DrugService;
import com.mahidol.drugapi.notification.models.NotificationMessage;
import com.mahidol.drugapi.notification.models.drug.DrugNotificationMessage;
import com.mahidol.drugapi.notification.models.drug.DrugSchedule;
import com.mahidol.drugapi.notification.repositories.DrugScheduleRepository;
import com.mahidol.drugapi.notification.services.NotificationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DrugCronSender {
    private static final Logger logger = LoggerFactory.getLogger(DrugCronSender.class);
    private final DrugScheduleRepository drugScheduleRepository;
    private final DrugService drugService;
    private final NotificationService notificationService;

    public DrugCronSender(
            DrugService drugService,
            DrugScheduleRepository drugScheduleRepository,
            NotificationService notificationService
    ) {
        this.drugService = drugService;
        this.drugScheduleRepository = drugScheduleRepository;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void scheduled() {
        long period = TimeUnit.MINUTES.toMillis(15);
        SchedulerService.scheduled(period, this::pushNotification);
    }

    public void pushNotification() {
        LocalTime time = ZonedDateTime.now(ZoneId.of("Asia/Bangkok")).toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        List<DrugSchedule> drugSchedules = readScheduled(time);
        List<NotificationMessage> messages = populateNotificationMessages(drugSchedules);

        messages.forEach(notificationService::send);
        logger.info("Start notification at: " + time);
        logger.info("All messages: " + messages);
    }

    private List<NotificationMessage> populateNotificationMessages(List<DrugSchedule> drugSchedules) {
        Map<UUID, Drug> drugMap = drugService.searchAllDrugByDrugsId(
                        null,
                        drugSchedules.stream()
                                .filter(DrugSchedule::getIsEnabled)
                                .map(DrugSchedule::getDrugId).toList()).stream()
//                .filter(Drug::getIsEnable)
                                .collect(Collectors.toMap(Drug::getId, drug -> drug));

        return drugSchedules.stream().flatMap(ds -> {
            Drug drug = drugMap.get(ds.getDrugId());
            if (drug != null)
                return Stream.of(new DrugNotificationMessage(ds.getDeviceToken(), DrugDTO.fromDrug(drug, Optional.empty())))
                        .map(notification -> (NotificationMessage) notification);
            else
                return Stream.empty();
        }).toList();
    }

    private List<DrugSchedule> readScheduled(LocalTime time) {
        return drugScheduleRepository.findByScheduledTime(time);
    }
}
