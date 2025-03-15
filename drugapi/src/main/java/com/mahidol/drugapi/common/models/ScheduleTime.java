package com.mahidol.drugapi.common.models;

import com.mahidol.drugapi.schedule.models.entities.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScheduleTime {
    private LocalTime time;
    private Boolean isEnabled = true;

    public static ScheduleTime fromSchedule(Schedule s) {
        return new ScheduleTime(s.getScheduleTime().toLocalTime(), s.getIsEnabled());
    }
}
