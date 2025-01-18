package models

import (
	"time"

	"github.com/google/uuid"
)

type History struct {
	ID         int64     `gorm:"primaryKey;autoIncrement"`
	UserID     uuid.UUID `gorm:"type:uuid;not null"`
	DrugID     uuid.UUID `gorm:"type:uuid;not null"`
	GroupID    *uuid.UUID `gorm:"type:uuid"`
	Status     string    `gorm:"type:text;check:status IN ('taken', 'missed', 'skipped')"`
	TakenAt    *time.Time
	NotifiedAt time.Time `gorm:"type:timestamp;not null"`
	Count      int       `gorm:"type:int;default:0;not null"`
	User       AppUser   `gorm:"foreignKey:UserID"`
}
