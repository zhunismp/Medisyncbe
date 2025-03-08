package services

import (
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"gorm.io/gorm"
)

type HistoryService struct {
	DB *gorm.DB
}

func NewHistoryService(db *gorm.DB) *HistoryService {
	return &HistoryService{DB: db}
}

func (h *HistoryService) GetAllHistory() []models.History {
    var histories *[]models.History
    h.DB.Find(&histories)

    return *histories
}

func (h *HistoryService) CreateHistory(schedules []models.Schedule) {
	for _, s := range schedules {
        if s.Type == 0 {
            if err := h.createDrugHistory(s); err != nil {
                fmt.Println("Error creating history record:", err)
            }
        } else {
            if err := h.createDrugGroupHistory(s); err != nil {
                fmt.Println("Error creating history record:", err)
            }
        }
    }
}

// Get all missed notification 3 times
func (h *HistoryService) GetIgnoredHistories(offset int) ([]models.History, error) {
	var histories []models.History	
	if err := h.DB.Where("status = ?", "missed").Where("count < ?", offset).Find(&histories).Error; err != nil {
        return nil, err
    }

	return histories, nil
}

func (h *HistoryService) createDrugHistory(s models.Schedule) (error) {
	return h.createHistoryRecord(s.User.ID, s.ReferenceID, nil, "missed", s.ScheduleTime, 0)
}

func (h *HistoryService) createDrugGroupHistory(s models.Schedule) (error) {
    
    var drugIds []uuid.UUID

    err := h.DB.Raw(`
        SELECT id
        FROM drug
        WHERE group_id = ?`, s.ReferenceID,
    ).Scan(&drugIds).Error

    if err != nil {
        fmt.Println("Error fetching drug IDs:", err)
        return err
    }

	for _, drugID := range drugIds {
        fmt.Println("Saved history for drug id: ", drugID, "in group id: ", s.ReferenceID)
		h.createHistoryRecord(s.User.ID, drugID, &s.ReferenceID, "missed", s.ScheduleTime, 0)
	}

	return nil
}

func (h *HistoryService) createHistoryRecord(userID, drugID uuid.UUID, groupID *uuid.UUID, status string, notifiedAt time.Time, count int) error {
    timeOnly := notifiedAt.Format("15:04:05")
    currentDate := time.Now().Format("2006-01-02")
    updatedNotifiedAt, err := time.Parse("2006-01-02 15:04:05", currentDate + " " + timeOnly)
    if err != nil {
        fmt.Println(err)
    }

    history := models.History{
        UserID:     userID,
        DrugID:     drugID,
        GroupID:    groupID,
        Status:     status,
        TakenAt:    nil,
        NotifiedAt: updatedNotifiedAt,
        Count:      count,
    }

    if err := h.DB.Create(&history).Error; err != nil {
        return err
    }

    return nil
}

func (h *HistoryService) GetHistoryByUserIDAndDate(userID uuid.UUID, date time.Time) ([]models.History, error) {
    var histories []models.History

    if err := h.DB.Where("user_id = ?", userID).Where("DATE(notified_at) = ?", date.Format("2006-01-02")).Find(&histories).Error; err != nil {
        return nil, err
    }

    return histories, nil
}