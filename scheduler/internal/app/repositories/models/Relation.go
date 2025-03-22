package models

import (
	"time"

	"github.com/google/uuid"
)

type Relation struct {
	ID         uuid.UUID  `gorm:"primaryKey"`
	UserID     uuid.UUID  `gorm:"type:uuid;not null"`
	RelativeID uuid.UUID  `gorm:"type:uuid;not null"`
	Relation   string     `gorm:"type:text;not null"`
	Notifiable bool       `gorm:"type:boolean;not null"`
	Readable   bool       `gorm:"type:boolean;not null"`
	CreateAt   *time.Time `gorm:"type:timestamp;not null"`

	User     AppUser `gorm:"foreignKey:UserID;constraint:OnDelete:CASCADE;"`
	Relative AppUser `gorm:"foreignKey:RelativeID;constraint:OnDelete:CASCADE;"`
}

func (Relation) TableName() string {
	return `relationship`
}
