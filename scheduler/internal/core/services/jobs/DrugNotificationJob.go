package jobs

import (
	"log"
	"time"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

type DrugNotificationJob struct {
	scheduleService *services.SchedulerService
	historyService *services.HistoryService
}

func NewDrugNotificationJob(
	scheduleService *services.SchedulerService, 
	historyService *services.HistoryService,
) *DrugNotificationJob {
	return &DrugNotificationJob{
		scheduleService: scheduleService,
		historyService: historyService,
	}
}


// Time in format "15:04:05"
func (j *DrugNotificationJob) Task(time time.Time) {
    schedules, err := j.scheduleService.GetScheduleWithTime(time)
    if err != nil {
        log.Println("Error fetching schedules:", err)
        return
    }

    j.historyService.CreateHistory(schedules)
}