package main

import (
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/connections"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
)

func main() {

	if err := setup(); err != nil {
		log.Println("Error setting up the application:", err)
		return
	}

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

func setup() error {
	cfg, err := config.LoadConfig()
	if err != nil {
		fmt.Println("Error loading configuration:", err)
		return err
	}

	if err := connections.Connect(cfg); err != nil {
		fmt.Println("Error connecting to the database:", err)
		return err
	}

	return nil
}