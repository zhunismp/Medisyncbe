package drug_jobs

import (
	"fmt"
	"log"
	"sync"
	"time"

	repoModels "github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
	coreModels "github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/utils"
)

type DrugNotificationJob struct {
	scheduleService *services.SchedulerService
	historyService *services.HistoryService
	notificationService *services.NotificationService
	config *config.Config
}

func NewDrugNotificationJob(
	scheduleService *services.SchedulerService, 
	historyService *services.HistoryService,
	notificationService *services.NotificationService,
	config *config.Config,
) *DrugNotificationJob {
	return &DrugNotificationJob{
		scheduleService: scheduleService,
		historyService: historyService,
		notificationService: notificationService,
		config: config,
	}
}

func (j *DrugNotificationJob) JobAttributes() coreModels.JobAttributes {
	return coreModels.JobAttributes {
		Name: "DrugNotificationJob",
		Interval: j.config.DrugNotificationInterval,
	}
}


func (j *DrugNotificationJob) Task(start time.Time, parameters ...interface{}) {
	fmt.Println("Job run at: ", start)

    schedules, err := j.scheduleService.GetScheduleWithTime(start)
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
        go func(schedule repoModels.Schedule) {
            defer wg.Done() 

            err := j.notificationService.SendNotification(
                schedule.User.RegisterToken,
				coreModels.Drug,
                "Time to take medicine",
                fmt.Sprintf("Your %s is ready for you to finish it", schedule.Name),
            )
            if err != nil {
                log.Printf("Error sending notification for schedule %s: %v", schedule.ID, err)
            }
        }(schedule)
    }

    wg.Wait()
    log.Println("All drugs notifications sent")
}