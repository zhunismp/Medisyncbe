package main

import (
	"fmt"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/connections"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/config"
)

func main() {

	cfg, err := config.LoadConfig()
	if err != nil {
		fmt.Println("Error loading configuration:", err)
		return
	}

	if err := connections.Connect(cfg); err != nil {
		fmt.Println("Error connecting to the database:", err)
		return
	}

	fmt.Println("Successfully connected to the database!")

}