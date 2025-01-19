package services

import (
	"fmt"
	"time"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"gorm.io/gorm"
)

type SchedulerService struct {
	DB *gorm.DB
}

func NewSchedulerService(db *gorm.DB) *SchedulerService {
	return &SchedulerService{DB: db}
}

func (s *SchedulerService) GetScheduleWithTime(t time.Time) ([]models.Schedule, error) {
	var schedules []models.Schedule
	fixedDate := time.Date(2000, time.January, 1, t.Hour(), t.Minute(), 0, 0, t.Location()) 

	result := s.DB.Where("schedule_time = ?", fixedDate).Find(&schedules)
	if result.Error != nil {
		return nil, fmt.Errorf("failed to fetch schedules for time %v: %w", t, result.Error)
	}

	return schedules, nil
}