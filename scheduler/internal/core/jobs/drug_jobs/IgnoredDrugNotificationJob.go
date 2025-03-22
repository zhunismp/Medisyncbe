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
	userService         *services.UserService
	config              *config.Config
}

func NewIgnoredDrugNotificationJob(
	historyService *services.HistoryService,
	relationService *services.RelationService,
	userService *services.UserService,
	notificationService *services.NotificationService,
	config *config.Config,
) *IgnoredDrugNotificationJob {
	return &IgnoredDrugNotificationJob{
		historyService:      historyService,
		notificationService: notificationService,
		relationService:     relationService,
		userService:         userService,
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
	unacceptableHistories := filteredByCount(normalHistories, 2)

	deduplicatedUsers := getDeduplicatedUsers(normalHistories)
	deduplicatedFriendsMap := j.getDeduplicatedRelatives(unacceptableHistories)

	log.Printf("[%s] Sending notifications for %d ignored histories", j.JobAttributes().Name, len(normalHistories))
	log.Printf("[%s] Sending notifications for %d unacceptable histories", j.JobAttributes().Name, len(unacceptableHistories))

	// sending notifications
	j.sendBatchNotifications(deduplicatedUsers)
	j.sendBatchNotificationsForFriends(deduplicatedFriendsMap)

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

func (j *IgnoredDrugNotificationJob) sendBatchNotificationsForFriends(relativesMap map[uuid.UUID][]repoModels.AppUser) {
	var wg sync.WaitGroup

	for userId, users := range relativesMap {
		wg.Add(1)

		// Pass userId and users as parameters to prevent variable capture issues
		go func(userId uuid.UUID, users []repoModels.AppUser) {
			defer wg.Done()

			userName, err := j.userService.GetUserByID(userId)
			if err != nil {
				log.Printf("Error fetching user %s: %v", userId, err)
				return
			}

			title := fmt.Sprintf("Your friend %s forget to take a medicine?", userName.FirstName)
			message := "Your friend wasn't able to take a medicine for 3 times. Do you want to help him?"

			for _, user := range users {
				err := j.notificationService.SendNotification(
					user.RegisterToken,
					coreModels.DrugTopic,
					title,
					message,
				)
				if err != nil {
					log.Printf("Error sending notification to user %s: %v", user.ID, err)
				}
			}
		}(userId, users)
	}

	wg.Wait()
}

func (j *IgnoredDrugNotificationJob) getDeduplicatedRelatives(histories []repoModels.History) map[uuid.UUID][]repoModels.AppUser {
	users := getDeduplicatedUsers(histories)

	relativesMap := make(map[uuid.UUID][]repoModels.AppUser)
	for _, u := range users {
		relativesMap[u.ID] = j.relationService.GetAuthorizedRelatives(u.ID)
	}

	return relativesMap
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

func filteredByTimes(t time.Time, histories []repoModels.History) []repoModels.History {
	filteredByTimes := make([]repoModels.History, 0)
	for _, history := range histories {
		minDiff := int(t.Sub(history.NotifiedAt).Minutes())

		if minDiff%10 == 0 {
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
