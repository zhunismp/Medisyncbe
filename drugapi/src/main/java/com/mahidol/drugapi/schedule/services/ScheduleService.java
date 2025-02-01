package com.mahidol.drugapi.schedule.services;

import com.mahidol.drugapi.common.models.ScheduleTime;
import com.mahidol.drugapi.drug.models.entites.Drug;
import com.mahidol.drugapi.druggroup.entities.DrugGroup;
import com.mahidol.drugapi.schedule.models.entities.Schedule;

import java.util.List;
import java.util.UUID;

public interface ScheduleService {
    void set(Drug drug, List<ScheduleTime> schedules);
    void set(DrugGroup drugGroup, List<ScheduleTime> schedules);
    void setIsEnabled(UUID referenceId, Boolean isEnabled);
    List<Schedule> get(UUID referenceId);
}
