package models

import (
	"time"

	"github.com/google/uuid"
)

type Schedule struct {
	ID           uuid.UUID `gorm:"type:uuid;primaryKey"`
	UserID       uuid.UUID `gorm:"type:uuid;not null"`
	ScheduleTime time.Time `gorm:"type:timestamp without time zone; not null"`
	Type         int       `gorm:"type:int;not null"`
	Name         string    `gorm:"type:text;not null"`
	ReferenceID  uuid.UUID `gorm:"type:uuid;not null"`
	IsEnabled    bool      `gorm:"type:boolean;not null"`
	User         AppUser   `gorm:"foreignKey:UserID;constraint:OnDelete:CASCADE;"`
}

func (Schedule) TableName() string {
	return `schedule`
}
