package jobs

import (
	"fmt"
	"log"
	"sync"
	"time"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/utils"
)

type DrugNotificationJob struct {
	scheduleService *services.SchedulerService
	historyService *services.HistoryService
	notificationService *services.NotificationService
}

func NewDrugNotificationJob(
	scheduleService *services.SchedulerService, 
	historyService *services.HistoryService,
	notificationService *services.NotificationService,
) *DrugNotificationJob {
	return &DrugNotificationJob{
		scheduleService: scheduleService,
		historyService: historyService,
		notificationService: notificationService,
	}
}


func (j *DrugNotificationJob) Task(time time.Time) {
	fmt.Println("Job run at: ", time)

    schedules, err := j.scheduleService.GetScheduleWithTime(time)
    if err != nil {
        log.Println("Error fetching schedules:", err)
        return
    }
	j.historyService.CreateHistory(schedules)

	utils.LogSchedules(schedules)
	utils.LogHistories(j.historyService.GetAllHistory())

    var wg sync.WaitGroup

    for _, schedule := range schedules {
        wg.Add(1)
        go func(schedule models.Schedule) {
            defer wg.Done() 

            err := j.notificationService.SendNotification(
                schedule.User.RegisterToken,
                "Time to take medicine",
                fmt.Sprintf("Your %s is ready for you to finish it", schedule.Name),
            )
            if err != nil {
                log.Printf("Error sending notification for schedule %s: %v", schedule.ID, err)
            }
        }(schedule)
    }

    wg.Wait()
    log.Println("All notifications sent")
}