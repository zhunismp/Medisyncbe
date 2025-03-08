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

		updatedStreak := calculateStreak(user.Streak, yesterdayHistories)

		if err := j.userService.UpdateUserStreak(user.ID, updatedStreak); err != nil {
			log.Println("Error updating user streak:", err)
		}
	}
}

func calculateStreak(oldStreak int, histories []models.History) int {
	if len(histories) == 0 {
		return oldStreak
	}

	for _, history := range histories {
		if history.Status != "taken" {
			return 0
		}
	}

	return oldStreak + 1
}