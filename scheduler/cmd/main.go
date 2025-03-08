package main

import (
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/connections"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/jobs"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
)

func main() {
	firebaseConfigPath := getFirebaseConfigPath()

	// Load configuration
	cfg, err := config.LoadConfig()
	if err != nil {
		fmt.Println("Error loading configuration:", err)
	}

	// Connect to the database
	if err := connections.Connect(cfg); err != nil {
		fmt.Println("Error connecting to the database:", err)
	}

	// Initialize services
	db := connections.DB
	schedulerService := services.NewSchedulerService(db)
	historyService := services.NewHistoryService(db)
	userService := services.NewUserService(db)
	notificationService, err := services.NewNotificationService(firebaseConfigPath)
	if err != nil {
		fmt.Println("Error initiate notification service: ", err)
	}

	// Initialize Jobs
	s, err := jobs.Initialize(
		schedulerService,
		historyService,
		notificationService,
		userService,
	)
	if err != nil {
		log.Println("Error initializing jobs:", err)
	}

	s.Start()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	<-sigChan
	log.Println("\nInterrupt signal received. Shutting down...")
}

func getFirebaseConfigPath() string {
	env := os.Getenv("ENV")

	if env == "prod" {
		return "/app/firebase-config.json"
	}

	return "./configs/firebase-config.json"
}
