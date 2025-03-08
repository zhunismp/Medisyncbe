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

func (s *UserService) GetAllUsers(id uuid.UUID) ([]models.AppUser, error) {
	var users []models.AppUser

	result := s.DB.Find(&users)
	if result.Error != nil {
		return nil, fmt.Errorf("failed to fetch users: %w", result.Error)
	}

	return users, nil
}