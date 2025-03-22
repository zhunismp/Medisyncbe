package models

import (
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type History struct {
	ID         uuid.UUID  `gorm:"type:uuid;primaryKey"`
	UserID     uuid.UUID  `gorm:"type:uuid;not null"`
	DrugID     uuid.UUID  `gorm:"type:uuid;not null"`
	GroupID    *uuid.UUID `gorm:"type:uuid"`
	Status     string     `gorm:"type:text;check:status IN ('taken', 'missed', 'skipped')"`
	TakenAt    *time.Time `gorm:"type:timestamp"`
	NotifiedAt time.Time  `gorm:"type:timestamp;not null"`
	Count      int        `gorm:"type:int;default:0;not null"`

	User AppUser `gorm:"foreignKey:UserID;constraint:OnDelete:CASCADE;"`
}

func (History) TableName() string {
	return `history`
}

func (h *History) BeforeCreate(tx *gorm.DB) (err error) {
	h.ID = uuid.New()
	return nil
}
