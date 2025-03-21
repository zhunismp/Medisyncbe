package services

import (
	"time"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"gorm.io/gorm"
)

type AppointmentService struct {
	DB *gorm.DB
}

func NewAppointmentService(db *gorm.DB) *AppointmentService {
	return &AppointmentService{DB: db}
}

func (a *AppointmentService) GetAppointmentsForTomorrow() ([]models.Appointment, error) {
	tomorrowStart := time.Now().Add(24 * time.Hour).Truncate(24 * time.Hour)
	tomorrowEnd := tomorrowStart.Add(24 * time.Hour)

	var appointments []models.Appointment
	err := a.DB.Preload("User").
		Where("datetime >= ? AND datetime < ?", tomorrowStart, tomorrowEnd).
		Find(&appointments).Error
	if err != nil {
		return nil, err
	}

	return appointments, nil
}