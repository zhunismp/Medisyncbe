package models

import (
	"time"

	"github.com/google/uuid"
)

type History struct {
	ID         uuid.UUID `gorm:"type:uuid;default:uuid_generate_v4();primaryKey"`
	UserID     uuid.UUID `gorm:"type:uuid;not null"`
	DrugID     uuid.UUID `gorm:"type:uuid;not null"`
	GroupID    *uuid.UUID `gorm:"type:uuid"`
	Status     string    `gorm:"type:text;check:status IN ('taken', 'missed', 'skipped')"`
	TakenAt    *time.Time `gorm:"type:timestamp"`
	NotifiedAt time.Time `gorm:"type:timestamp;not null"`
	Count      int       `gorm:"type:int;default:0;not null"`
	User       AppUser   `gorm:"foreignKey:UserID"`
}
