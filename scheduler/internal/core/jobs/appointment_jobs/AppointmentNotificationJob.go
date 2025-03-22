package appointment_jobs

import (
	"fmt"
	"log"
	"sync"
	"time"

	repoModels "github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
	coreModels "github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

type AppointmentNotificationJob struct {
	appointmemtService  *services.AppointmentService
	notificationService *services.NotificationService
	config              *config.Config
}

func NewAppointmentNotificationJob(
	appointmemtService *services.AppointmentService,
	notificationService *services.NotificationService,
	config *config.Config,
) *AppointmentNotificationJob {
	return &AppointmentNotificationJob{
		appointmemtService:  appointmemtService,
		notificationService: notificationService,
		config:              config,
	}
}

func (j *AppointmentNotificationJob) JobAttributes() coreModels.JobAttributes {
	return coreModels.JobAttributes{
		Name:     "AppointmentNotificationJob",
		Interval: j.config.AppointmentNotificationInterval,
	}
}

func (j *AppointmentNotificationJob) Task(start time.Time) {
	appointments, err := j.appointmemtService.GetAppointmentsForTomorrow()
	if err != nil {
		log.Println("Error fetching appointments:", err)
		return
	}

	log.Printf("Sending notifications for %d appointments", len(appointments))

	var wg sync.WaitGroup

	for _, appointment := range appointments {
		wg.Add(1)
		go func(appointment repoModels.Appointment) {
			defer wg.Done()

			err := j.notificationService.SendNotification(
				appointment.User.RegisterToken,
				coreModels.AppointmentTopic,
				"Appointment Reminder",
				fmt.Sprintf("Don't forget tomorrow %s appointment. at time %s", appointment.Title, appointment.Datetime),
			)
			if err != nil {
				log.Printf("Error sending notification for schedule %s: %v", appointment.ID, err)
			}
		}(appointment)
	}

	wg.Wait()
}
