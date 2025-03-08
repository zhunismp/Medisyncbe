package services

import (
	"fmt"

	"github.com/google/uuid"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	"gorm.io/gorm"
)

type UserService struct {
	DB *gorm.DB
}

func NewUserService(db *gorm.DB) *UserService {
	return &UserService{DB: db}
}

func (s *UserService) GetRegisterTokenByID(id uuid.UUID) (string, error) {
	var user models.AppUser

	result := s.DB.Where("id = ?", id).First(&user)
	if result.Error != nil {
		return "", fmt.Errorf("failed to fetch user with id %v: %w", id, result.Error)
	}

	return user.RegisterToken, nil
}

func (s *UserService) GetAllUsers() ([]models.AppUser, error) {
	var users []models.AppUser

	result := s.DB.Find(&users)
	if result.Error != nil {
		return nil, fmt.Errorf("failed to fetch users: %w", result.Error)
	}

	return users, nil
}

func (s *UserService) UpdateUserStreak( userID uuid.UUID, streak int) error {
	result := s.DB.Model(&models.AppUser{}).Where("id = ?", userID).Update("streak", streak)
	
	if result.Error != nil {
		return fmt.Errorf("failed to update streak for user ID %s: %w", userID, result.Error)
	}

	if result.RowsAffected == 0 {
		return fmt.Errorf("no user found with ID %s or no change in streak value", userID)
	}
	return nil
}