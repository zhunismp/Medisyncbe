package models

import (
	"time"

	"github.com/google/uuid"
)

type Appointment struct {
	ID 		    uuid.UUID      `gorm:"type:uuid;primaryKey"`
	userId	    uuid.UUID      `gorm:"type:uuid;not null"`
	title	    string         `gorm:"type:text;not null"`
	medicName   string         `gorm:"type:text"`
	datetime    time.Time      `gorm:"type:timestamp;not null"`
	destination string 		   `gorm:"type:text"`
	remarks     string         `gorm:"type:text"`
}