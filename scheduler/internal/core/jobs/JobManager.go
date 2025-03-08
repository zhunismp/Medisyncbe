package jobs

import (
	"time"

	"github.com/go-co-op/gocron/v2"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/jobs/drug_jobs"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

func Initialize(
	schedulerService *services.SchedulerService,
	historyService *services.HistoryService,
	notificationService *services.NotificationService,
) gocron.Scheduler {
	drugNotificationJob := drug_jobs.NewDrugNotificationJob(
		schedulerService,
		historyService,
		notificationService,
	)

	jobs := []models.BaseJob{
		drugNotificationJob,
	}

	return schedule(jobs)
}

func schedule(jobs []models.BaseJob) gocron.Scheduler {
	s, _ := gocron.NewScheduler()

	for _, job := range jobs {
		attributes := job.JobAttributes()
		s.NewJob(
			gocron.CronJob(
				attributes.Interval,
				true,
			),
			gocron.NewTask(job.Task, time.Now().Truncate(time.Minute)),
			gocron.WithName(attributes.Name),
		)
	}

	return s
}
