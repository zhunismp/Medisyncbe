package jobs

import (
	"log"
	"time"

	"github.com/go-co-op/gocron/v2"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/jobs/appointment_jobs"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/jobs/drug_jobs"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/jobs/user_jobs"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

func Initialize(
	schedulerService *services.SchedulerService,
	historyService *services.HistoryService,
	notificationService *services.NotificationService,
	appointmentService *services.AppointmentService,
	userService *services.UserService,
	config *config.Config,
) (gocron.Scheduler, error) {
	drugNotificationJob := drug_jobs.NewDrugNotificationJob(
		schedulerService,
		historyService,
		notificationService,
		config,
	)
	appointmentNotificationJob := appointment_jobs.NewAppointmentNotificationJob(
		appointmentService,
		notificationService,
		config,
	)
	userStreakJob := user_jobs.NewUserStreakJob(
		historyService,
		userService,
		config,
	)

	jobs := []models.BaseJob{
		drugNotificationJob,
		appointmentNotificationJob,
		userStreakJob,
	}

	return schedule(jobs)
}

func schedule(jobs []models.BaseJob) (gocron.Scheduler, error) {
	s, err := gocron.NewScheduler()
	if err != nil {
		log.Println("Error creating scheduler:", err)
		return nil, err
	}

	for _, job := range jobs {
		attributes := job.JobAttributes()
		s.NewJob(
			gocron.CronJob(
				attributes.Interval,
				true,
			),
			gocron.NewTask(func() {
				job.Task(time.Now().Truncate(time.Minute))
			}),
			gocron.WithName(attributes.Name),
		)
	}

	return s, nil
}
