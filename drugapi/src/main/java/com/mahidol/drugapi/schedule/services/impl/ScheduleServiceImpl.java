package com.mahidol.drugapi.schedule.services.impl;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.schedule.models.entities.Schedule;
import com.mahidol.drugapi.schedule.repositories.ScheduleRepository;
import com.mahidol.drugapi.schedule.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void set(Drug drug, List<ScheduleTime> schedules) {
        scheduleRepository.deleteAllByReferenceId(drug.getId());

        // Return to avoid saving empty rows to db
        if (schedules.isEmpty())
            return;

        List<Schedule> s = schedules.stream().map(st -> new Schedule()
                .setScheduleTime(LocalDateTime.of(dummyDate, st.getTime()))
                .setType(0)
                .setName(drug.getGenericName())
                .setUserId(drug.getUserId())
                .setIsEnabled(st.getIsEnabled())
                .setReferenceId(drug.getId())).toList();

        scheduleRepository.saveAll(s);
    }

    @Override
    public void set(DrugGroup drugGroup, List<ScheduleTime> schedules) {
        scheduleRepository.deleteAllByReferenceId(drugGroup.getId());

        // Return to avoid saving empty rows to db
        if(schedules.isEmpty())
            return;

        List<Schedule> s = schedules.stream().map(st -> new Schedule()
                .setScheduleTime(LocalDateTime.of(dummyDate, st.getTime()))
                .setType(1)
                .setName(drugGroup.getGroupName())
                .setUserId(drugGroup.getUserId())
                .setIsEnabled(st.getIsEnabled())
                .setReferenceId(drugGroup.getId())).toList();

        scheduleRepository.saveAll(s);
    }

    @Override
    public void setIsEnabled(UUID referenceId, Boolean isEnabled) {
        List<Schedule> s = scheduleRepository.findAllByReferenceId(referenceId);
        s.forEach(sch -> sch.setIsEnabled(isEnabled));

        scheduleRepository.saveAll(s);
    }

    @Override
    public List<Schedule> get(UUID referenceId) {
        return scheduleRepository.findAllByReferenceId(referenceId);
    }
}
