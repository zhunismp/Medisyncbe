package drug_jobs

import (
	"fmt"
	"log"
	"sync"
	"time"

	"github.com/google/uuid"
	repoModels "github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
	coreModels "github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

type IgnoredDrugNotificationJob struct {
	historyService *services.HistoryService
	notificationService *services.NotificationService
	config *config.Config
}

func NewIgnoredDrugNotificationJob(
	historyService *services.HistoryService,
	notificationService *services.NotificationService,
	config *config.Config,
) *IgnoredDrugNotificationJob {
	return &IgnoredDrugNotificationJob{
		historyService: historyService,
		notificationService: notificationService,
		config: config,
	}
}

func (j *IgnoredDrugNotificationJob) JobAttributes() coreModels.JobAttributes {
	return coreModels.JobAttributes {
		Name: "IgnoredDrugNotificationJob",
		Interval: j.config.IgnoredDrugNotificationInterval,
	}
}

func (j *IgnoredDrugNotificationJob) Task(start time.Time, parameters ...interface{}) {
	ignoredHistories, err := j.historyService.GetIgnoredHistories()
	if err != nil {
		log.Println("Error fetching ignored histories: ", err)
		return
	}

	// filtered only passed by 5, 10, 15 minutes
	filteredByTimes := filteredByTimes(start, ignoredHistories)

	// increment count for each history
	err = j.historyService.BatchIncrementCount(filteredByTimes, 1)
	if err != nil {
		log.Printf("Error incrementing count for histories: %v", err)
	}

	deduplicatedUsers := getDeduplicatedUsers(filteredByTimes)

	var wg sync.WaitGroup
	for _, user := range deduplicatedUsers {
        wg.Add(1)
        go func(user repoModels.AppUser) {
            defer wg.Done() 

            err := j.notificationService.SendNotification(
                user.RegisterToken,
				coreModels.Drug,
                "Don't Forget to take your medicine",
                fmt.Sprintf("Hi there %s! do you forget something?", user.FirstName),
            )
            if err != nil {
                log.Printf("Error resending notification for user %s: %v", user.ID, err)
            }
        }(user)
    }

    wg.Wait()
    log.Println("All ignored drug notifications sent")


}

func filteredByTimes(t time.Time, histories []repoModels.History) []repoModels.History {
	filteredByTimes := make([]repoModels.History, 0)
	for _, history := range histories {
		minDiff := int(t.Sub(history.NotifiedAt).Minutes())

		if minDiff % 5 == 0 {
			filteredByTimes = append(filteredByTimes, history)
		}
	}

	return filteredByTimes
}

func getDeduplicatedUsers(histories []repoModels.History) []repoModels.AppUser {
	userMap := make(map[uuid.UUID]repoModels.AppUser) // Map to store unique users by ID

	for _, history := range histories {
		if _, exists := userMap[history.UserID]; !exists { // Check if user already exists
			userMap[history.UserID] = history.User // Store user if not present
		}
	}

	// Convert map values to slice
	deduplicatedUsers := make([]repoModels.AppUser, 0, len(userMap))
	for _, user := range userMap {
		deduplicatedUsers = append(deduplicatedUsers, user)
	}

	return deduplicatedUsers
}

