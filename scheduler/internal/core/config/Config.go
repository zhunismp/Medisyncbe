package config

import "google.golang.org/api/option"

type Config struct {
	DBUser     string
	DBPassword string
	DBName     string
	DBHost     string
	DBPort     string

	DrugNotificationInterval        string
	IgnoredDrugNotificationInterval string
	AppointmentNotificationInterval string
	UserStreakInterval              string

	FirebaseClientOption option.ClientOption
}
