package models

import "google.golang.org/api/option"

type Config struct {
	DBUser     				string
	DBPassword 				string
	DBName     				string
	DBHost     			 	string
	DBPort     			 	string

	FirebaseClientOption 	option.ClientOption
}