package main

import (
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-co-op/gocron/v2"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/connections"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/services/jobs"
)

func main() {
	firebaseConfigPath := "./configs/firebase-config.json"

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
	notificationService, err := services.NewNotificationService(firebaseConfigPath)
	if err != nil {
		fmt.Println("Error initiate notification service: ", err)
	}

	// Initialize jobs
	drugNotificationJob := jobs.NewDrugNotificationJob(
		schedulerService, 
		historyService,
		notificationService,
	)

	// Initialize scheduler
	s, err := gocron.NewScheduler()
	if err != nil {
		log.Println("Error creating scheduler:", err)
		return
	}

	// Add DrugNotificationJob 
	s.NewJob(
		gocron.CronJob(
			"*/1 * * * *",
			true,
		),
		gocron.NewTask(
			func() {
				now := time.Now().Truncate(time.Minute) 
    			drugNotificationJob.Task(now)
			},
		),
		gocron.WithName("DrugNotificationJob"),
	)

	s.Start()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		<- sigChan 
		log.Println("\nInterrupt signal received. Shutting down...")
		os.Exit(0)
	}()

	for {

	}
}