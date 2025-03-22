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
	historyService      *services.HistoryService
	relationService     *services.RelationService
	notificationService *services.NotificationService
	config              *config.Config
}

func NewIgnoredDrugNotificationJob(
	historyService *services.HistoryService,
	notificationService *services.NotificationService,
	config *config.Config,
) *IgnoredDrugNotificationJob {
	return &IgnoredDrugNotificationJob{
		historyService:      historyService,
		notificationService: notificationService,
		config:              config,
	}
}

func (j *IgnoredDrugNotificationJob) JobAttributes() coreModels.JobAttributes {
	return coreModels.JobAttributes{
		Name:     "IgnoredDrugNotificationJob",
		Interval: j.config.IgnoredDrugNotificationInterval,
	}
}

func (j *IgnoredDrugNotificationJob) Task(start time.Time) {
	ignoredHistories, err := j.historyService.GetIgnoredHistories()
	if err != nil {
		log.Println("Error fetching ignored histories: ", err)
		return
	}

	normalHistories := filteredByTimes(start, ignoredHistories)
	unacceptableHistories := filteredByCount(normalHistories, 3)

	deduplicatedUsers := getDeduplicatedUsers(normalHistories)
	deduplicatedFriends := getDeduplicatedRelatives(j.relationService, unacceptableHistories)

	log.Printf("[%s] Sending notifications for %d ignored histories", j.JobAttributes().Name, len(normalHistories))
	log.Printf("[%s] Sending notifications for %d unacceptable histories", j.JobAttributes().Name, len(unacceptableHistories))

	// sending notifications
	j.sendBatchNotifications(deduplicatedUsers)
	j.sendBatchNotifications(deduplicatedFriends)

	// increment count after sending notifications
	err = j.historyService.BatchIncrementCount(normalHistories, 1)
	if err != nil {
		log.Printf("Error incrementing count for histories: %v", err)
	}
}

func (j *IgnoredDrugNotificationJob) sendBatchNotifications(users []repoModels.AppUser) {
	var wg sync.WaitGroup
	for _, user := range users {
		wg.Add(1)
		go func(user repoModels.AppUser) {
			defer wg.Done()

			err := j.notificationService.SendNotification(
				user.RegisterToken,
				coreModels.DrugTopic,
				"Don't Forget to take your medicine",
				fmt.Sprintf("Hi there %s! do you forget something?", user.FirstName),
			)
			if err != nil {
				log.Printf("Error resending notification for user %s: %v", user.ID, err)
			}
		}(user)
	}

	wg.Wait()
}

func filteredByTimes(t time.Time, histories []repoModels.History) []repoModels.History {
	filteredByTimes := make([]repoModels.History, 0)
	for _, history := range histories {
		minDiff := int(t.Sub(history.NotifiedAt).Minutes())

		if minDiff % 10 == 0 {
			filteredByTimes = append(filteredByTimes, history)
		}
	}

	return filteredByTimes
}

func filteredByCount(histories []repoModels.History, count int) []repoModels.History {
	filteredByCount := make([]repoModels.History, 0)
	for _, history := range histories {
		if history.Count == count {
			filteredByCount = append(filteredByCount, history)
		}
	}

	return filteredByCount
}

func getDeduplicatedUsers(histories []repoModels.History) []repoModels.AppUser {
	userMap := make(map[uuid.UUID]repoModels.AppUser)

	for _, history := range histories {
		if _, exists := userMap[history.UserID]; !exists {
			userMap[history.UserID] = history.User
		}
	}

	deduplicatedUsers := make([]repoModels.AppUser, 0, len(userMap))
	for _, user := range userMap {
		deduplicatedUsers = append(deduplicatedUsers, user)
	}

	return deduplicatedUsers
}

func getDeduplicatedRelatives(relationService *services.RelationService, histories []repoModels.History) []repoModels.AppUser {
	users := getDeduplicatedUsers(histories)

	var relatives []repoModels.AppUser
	for _, u := range users {
		relatives = append(relatives, relationService.GetAuthorizedRelatives(u.ID)...)
	}

	return relatives
}
