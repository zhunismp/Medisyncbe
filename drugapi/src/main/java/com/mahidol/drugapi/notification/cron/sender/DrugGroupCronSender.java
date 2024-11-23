package com.mahidol.drugapi.notification.cron.sender;

import com.mahidol.drugapi.common.services.SchedulerService;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
import com.mahidol.drugapi.druggroup.services.DrugGroupService;
import com.mahidol.drugapi.notification.models.NotificationMessage;
import com.mahidol.drugapi.notification.models.druggroup.DrugGroupNotificationMessage;
import com.mahidol.drugapi.notification.models.druggroup.DrugGroupSchedule;
import com.mahidol.drugapi.notification.repositories.DrugGroupScheduleRepository;
import com.mahidol.drugapi.notification.services.NotificationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DrugGroupCronSender {
    private static final Logger logger = LoggerFactory.getLogger(DrugGroupCronSender.class);
    private final NotificationService notificationService;
    private final DrugGroupService drugGroupService;
    private final DrugGroupScheduleRepository drugScheduleRepository;

    public DrugGroupCronSender(
            NotificationService notificationService,
            DrugGroupService drugGroupService,
            DrugGroupScheduleRepository drugGroupScheduleRepository
    ) {
        this.notificationService = notificationService;
        this.drugGroupService = drugGroupService;
        this.drugScheduleRepository = drugGroupScheduleRepository;
    }

    @PostConstruct
    public void scheduled() {
        long period = TimeUnit.MINUTES.toMillis(15);
        SchedulerService.scheduled(period, this::pushNotification);
    }

    public void pushNotification() {
        LocalTime time = ZonedDateTime.now(ZoneId.of("Asia/Bangkok")).toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        List<DrugGroupSchedule> drugGroupSchedules = readScheduled(time);
        List<NotificationMessage> messages = populateNotificationMessages(drugGroupSchedules);

        messages.forEach(notificationService::send);
        logger.info("Start notification at: " + time);
        logger.info("All messages: " + messages);
    }

    private List<NotificationMessage> populateNotificationMessages(List<DrugGroupSchedule> schedules) {
        Map<UUID, DrugGroupDTO> drugGroupMap = drugGroupService.searchAllDrugGroupByDrugGroupIds(
                        schedules.stream().map(DrugGroupSchedule::getDrugGroupId).toList()).stream()
                .filter(DrugGroupDTO::getIsEnabled)
                .collect(Collectors.toMap(DrugGroupDTO::getId, drugGroupDTO -> drugGroupDTO));

        return schedules.stream().flatMap(s -> {
            DrugGroupDTO drugGroupDTO = drugGroupMap.get(s.getDrugGroupId());

            if (drugGroupDTO != null)
                return Stream.of(new DrugGroupNotificationMessage(s.getDeviceId(), drugGroupDTO))
                        .map(notification -> (NotificationMessage) notification);
            else
                return Stream.empty();
        }).toList();
    }

    private List<DrugGroupSchedule> readScheduled(LocalTime time) {
        return drugScheduleRepository.findByScheduledTime(time);
    }
}
