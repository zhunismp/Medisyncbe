package config

import (
	"encoding/base64"
	"fmt"
	"os"
	"strings"

	"github.com/joho/godotenv"
	"google.golang.org/api/option"
)

func LoadConfig() (*Config, error) {
	var clientOption option.ClientOption

	if os.Getenv("ENV") != "prod" {
		if err := godotenv.Load("./configs/.env"); err != nil {
			return nil, fmt.Errorf("failed to load .env file: %w", err)
		}
	}

	clientOption, err := loadClientOption()
	if err != nil {
		return nil, fmt.Errorf("failed to load client option: %w", err)
	}

	return &Config{
		DBUser:                          os.Getenv("POSTGRES_USER"),
		DBPassword:                      os.Getenv("POSTGRES_PASSWORD"),
		DBName:                          os.Getenv("POSTGRES_DB"),
		DBHost:                          os.Getenv("POSTGRES_HOST"),
		DBPort:                          os.Getenv("POSTGRES_PORT"),
		DrugNotificationInterval:        getOrFallback("DRUG_NOTIFICATION_INTERVAL", "*/1 * * * *"),
		IgnoredDrugNotificationInterval: getOrFallback("IGNORED_DRUG_NOTIFICATION_INTERVAL", "*/1 * * * *"),
		AppointmentNotificationInterval: getOrFallback("APPOINTMENT_NOTIFICATION_INTERVAL", "0 1 * * *"),
		UserStreakInterval:              getOrFallback("USER_STREAK_INTERVAL", "0 1 * * *"),
		FirebaseClientOption:            clientOption,
	}, nil
}

func loadClientOption() (option.ClientOption, error) {
	if os.Getenv("ENV") != "prod" {
		return option.WithCredentialsFile("./configs/firebase-config.json"), nil
	}

	firebaseConfigBase64 := os.Getenv("FIREBASE_CONFIG_BASE64")
	firebaseConfigBase64 = strings.ReplaceAll(firebaseConfigBase64, " ", "")
	if firebaseConfigBase64 == "" {
		return nil, fmt.Errorf("missing firebase configurations base64 file")
	}

	firebaseConfigJSON, err := base64.StdEncoding.DecodeString(firebaseConfigBase64)
	if err != nil {
		return nil, fmt.Errorf("failed to decode Firebase config: %w", err)
	}

	return option.WithCredentialsJSON(firebaseConfigJSON), nil
}

func getOrFallback(key string, defaultValue string) string {
	value := os.Getenv(key)
	if value == "" {
		return defaultValue
	}
	return value
}
