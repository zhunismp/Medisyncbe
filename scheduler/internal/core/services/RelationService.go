package services

import (
	"log"

	"github.com/google/uuid"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"gorm.io/gorm"
)

type RelationService struct {
	DB *gorm.DB
}

func NewRelationService(db *gorm.DB) *RelationService {
	return &RelationService{DB: db}
}

func (r *RelationService) GetAuthorizedRelatives(userId uuid.UUID) []models.AppUser {
	var validRelativeIDs []uuid.UUID
	err := r.DB.Table("relations as r1").
		Select("r1.relative_id").
		Joins("JOIN relations as r2 ON r1.relative_id = r2.user_id AND r1.user_id = r2.relative_id").
		Where("r1.user_id = ? AND r1.notifiable = ? AND r2.notifiable = ?", userId, true, true).
		Pluck("r1.relative_id", &validRelativeIDs).Error
	if err != nil {
		return []models.AppUser{}
	}

	var validRelatives []models.AppUser
	if len(validRelativeIDs) > 0 {
		r.DB.Where("id IN ?", validRelativeIDs).Find(&validRelatives)
	}

	log.Printf("Relatives for user %s: %v", userId, validRelatives)

	return validRelatives
}
