package user_jobs

import (
	coreModels "github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

type UserStreakJob struct {
	schedulerService *services.SchedulerService
}

func NewUserStreakJob(schedulerService *services.SchedulerService) *UserStreakJob {
	return &UserStreakJob{
		schedulerService: schedulerService,
	}
}

func (j *UserStreakJob) JobAttributes() coreModels.JobAttributes {
	return coreModels.JobAttributes {
		Name: "UserStreakJob",
		Interval: "0 10 * * *",
	}
}