package models

import (
	"time"

	"github.com/google/uuid"
)

type Appointment struct {
	ID 		    uuid.UUID      `gorm:"type:uuid;primaryKey"`
	UserId	    uuid.UUID      `gorm:"type:uuid;not null"`
	Title	    string         `gorm:"type:text;not null"`
	MedicName   string         `gorm:"type:text"`
	Datetime    time.Time      `gorm:"type:timestamp;not null"`
	Destination string 		   `gorm:"type:text"`
	Remarks     string         `gorm:"type:text"`
	User       	 AppUser   	   `gorm:"foreignKey:UserID;constraint:OnDelete:CASCADE;"`
}