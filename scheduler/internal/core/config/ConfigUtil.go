package config

import (
	"fmt"
	"os"

	"github.com/joho/godotenv"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
)

func LoadConfig() (*models.Config, error) {
	if os.Getenv("ENV") != "prod" {
		if err := godotenv.Load("./configs/.env"); err != nil {
			return nil, fmt.Errorf("failed to load .env file: %w", err)
		}
	}

	return &models.Config{
		DBUser:     os.Getenv("POSTGRES_USER"),
		DBPassword: os.Getenv("POSTGRES_PASSWORD"),
		DBName:     os.Getenv("POSTGRES_DB"),
		DBHost:     os.Getenv("POSTGRES_HOST"),
		DBPort:     os.Getenv("POSTGRES_PORT"),
	}, nil
}