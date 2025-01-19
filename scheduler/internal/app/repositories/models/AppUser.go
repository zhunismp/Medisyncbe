package models

import (
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type AppUser struct {
	ID             uuid.UUID      `gorm:"type:uuid;primaryKey"`
	RegisterToken  string         `gorm:"type:text"`
	FirstName      string         `gorm:"type:text;not null"`
	LastName       string         `gorm:"type:text"`
	BirthDate      time.Time      `gorm:"type:date;not null"`
	Weight         float32        `gorm:"type:float"`
	Height         float32        `gorm:"type:float"`
	Gender         string         `gorm:"type:char;not null"`
	BloodGroup     string         `gorm:"type:varchar(5);not null"`
	HealthCondition string        `gorm:"type:text"`
	DrugAllergy    string         `gorm:"type:text"`
	FoodAllergy    string         `gorm:"type:text"`
	CreatedAt      time.Time
	UpdatedAt      time.Time
	DeletedAt      gorm.DeletedAt `gorm:"index"`
}

func (AppUser) TableName() string {
	return `app_user`
}