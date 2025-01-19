package models

import (
	"github.com/google/uuid"
)

type DrugGroup struct {
	ID       uuid.UUID  `gorm:"type:uuid;primaryKey"`
	UserID   uuid.UUID  `gorm:"type:uuid;not null"`
	GroupName string     `gorm:"type:text;not null"`
	DrugIDs  *[]uuid.UUID `gorm:"type:uuid[]"`
}

func (DrugGroup) TableName() string {
	return `drug_group`
}