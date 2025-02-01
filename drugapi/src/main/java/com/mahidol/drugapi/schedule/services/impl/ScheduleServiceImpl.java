package com.mahidol.drugapi.schedule.services.impl;

import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.schedule.models.entities.Schedule;
import com.mahidol.drugapi.schedule.repositories.ScheduleRepository;
import com.mahidol.drugapi.schedule.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // Dummy date due to scheduler incompatible with time datatype
    private final LocalDate dummyDate = LocalDate.of(2000, 1, 1);

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void set(Drug drug, List<LocalTime> schedules, Boolean isEnabled) {
        scheduleRepository.deleteAllByReferenceId(drug.getId());

        // Return to avoid saving empty rows to db
        if (schedules.isEmpty())
            return;

        List<Schedule> s = schedules.stream().map(t -> new Schedule()
                .setScheduleTime(LocalDateTime.of(dummyDate, t))
                .setType(0)
                .setName(drug.getGenericName())
                .setUserId(drug.getUserId())
                .setIsEnabled(isEnabled)
                .setReferenceId(drug.getId())).toList();

        scheduleRepository.saveAll(s);
    }

    @Override
    public void set(DrugGroup drugGroup, List<LocalTime> schedules, Boolean isEnabled) {
        scheduleRepository.deleteAllByReferenceId(drugGroup.getId());

        // Return to avoid saving empty rows to db
        if(schedules.isEmpty())
            return;

        List<Schedule> s = schedules.stream().map(t -> new Schedule()
                .setScheduleTime(LocalDateTime.of(dummyDate, t))
                .setType(1)
                .setName(drugGroup.getGroupName())
                .setUserId(drugGroup.getUserId())
                .setIsEnabled(isEnabled)
                .setReferenceId(drugGroup.getId())).toList();

        scheduleRepository.saveAll(s);
    }

    @Override
    public List<Schedule> get(UUID referenceId) {
        return scheduleRepository.findAllByReferenceId(referenceId);
    }
}
