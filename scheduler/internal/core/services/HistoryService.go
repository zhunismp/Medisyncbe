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

func (h *HistoryService) CreateHistory(schedules []models.Schedule) {
	for _, s := range schedules {
        if s.Type != 0 {
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
	return h.createHistoryRecord(s.UserID, s.ReferenceID, nil, "missed", s.ScheduleTime, 0)
}

func (h *HistoryService) createDrugGroupHistory(s models.Schedule) (error) {
	var drugIds = []uuid.UUID{}
	h.DB.Model(&models.DrugGroup{}).Where("id = ?", s.ReferenceID).Select("drug_id").Find(&drugIds)

	for _, drugID := range drugIds {
		h.createHistoryRecord(s.UserID, drugID, &s.ReferenceID, "missed", s.ScheduleTime, 0)
	}

	return nil
}

func (h *HistoryService) createHistoryRecord(userID, drugID uuid.UUID, groupID *uuid.UUID, status string, notifiedAt time.Time, count int) error {
    history := models.History{
        UserID:     userID,
        DrugID:     drugID,
        GroupID:    groupID,
        Status:     status,
        TakenAt:    nil,
        NotifiedAt: notifiedAt,
        Count:      count,
    }

    if err := h.DB.Create(&history).Error; err != nil {
        return err
    }

    return nil
}
