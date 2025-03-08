package user_jobs

import (
	"log"
	"time"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	coreModels "github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

type UserStreakJob struct {
	historyService *services.HistoryService
	userService *services.UserService
}

func NewUserStreakJob(
	hisotrySerivce *services.HistoryService,
	userService *services.UserService,
) *UserStreakJob {
	return &UserStreakJob{
		historyService: hisotrySerivce,
		userService: userService,
	}
}

func (j *UserStreakJob) JobAttributes() coreModels.JobAttributes {
	return coreModels.JobAttributes {
		Name: "UserStreakJob",
		Interval: "0 0 * * *",
	}
}

func (j *UserStreakJob) Task(start time.Time, parameters ...interface{}) {
	users, err := j.userService.GetAllUsers()
	if err != nil {
		log.Println("Error fetching users:", err)
		return
	}
	lastestDate := start.AddDate(0, 0, -1)

	for _, user := range users {
		yesterdayHistories, err := j.historyService.GetHistoryByUserIDAndDate(user.ID, lastestDate)
		if err != nil {
			log.Println("Error fetching user histories:", err)
			continue
		}

		var updatedStreak int
		if isContinued(yesterdayHistories) {
			updatedStreak = user.Streak + 1
		} else {
			updatedStreak = 0
		}

		if err := j.userService.UpdateUserStreak(user.ID, updatedStreak); err != nil {
			log.Println("Error updating user streak:", err)
		}
	}
}

func isContinued(histories []models.History) bool {
	for _, history := range histories {
		if history.Status != "taken" {
			return false
		}
	}

	return true
}