package connections

import (
	"fmt"
	"log"
	"time"

	dbmodels "github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
	amodels "github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var DB *gorm.DB

func Connect(cfg *amodels.Config) error {
	dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		cfg.DBHost, cfg.DBUser, cfg.DBPassword, cfg.DBName, cfg.DBPort)
	
	maxRetries := 5
	retryDelay := 2 * time.Second

	var db *gorm.DB
	var err error
	for i := 0; i < maxRetries; i++ {
		db, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
		if err == nil {
			break
		}

		log.Printf("Failed to connect to database. Retrying in %s... (attempt %d/%d)", retryDelay, i+1, maxRetries)
		time.Sleep(retryDelay)
	}

	if err != nil {
		return fmt.Errorf("failed to connect to database after %d attempts: %w", maxRetries, err)
	}

	if err := db.AutoMigrate(
		&dbmodels.AppUser{},
		&dbmodels.DrugGroup{},
		&dbmodels.Schedule{},
		&dbmodels.History{},
	); err != nil {
		return fmt.Errorf("failed to migrate models: %w", err)
	}

	DB = db
	log.Println("Database connected successfully!")
	return nil
}