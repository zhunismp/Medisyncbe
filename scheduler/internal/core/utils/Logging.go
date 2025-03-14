package utils

import (
	"fmt"
	"time"

	"github.com/zhunismp/Medisyncbe/scheduler/internal/app/repositories/models"
)

func LogSchedules(schedules []models.Schedule) {
	if len(schedules) == 0 {
		fmt.Println("No schedules to log.")
		return
	}

	for _, schedule := range schedules {
		fmt.Printf("Schedule ID: %s\n", schedule.ID)
		fmt.Printf("User ID: %s\n", schedule.User.ID)
		fmt.Printf("Schedule Time: %s\n", schedule.ScheduleTime.Format("15:04:05")) // Format as HH:mm:ss
		fmt.Printf("Type: %d\n", schedule.Type)
		fmt.Printf("Name: %s\n", schedule.Name)
		fmt.Printf("Reference ID: %s\n", schedule.ReferenceID)
		fmt.Printf("Is Enabled: %t\n", schedule.IsEnabled)
		fmt.Println("-----------------------------------")
	}
}

func LogHistories(histories []models.History) {
	if len(histories) == 0 {
		fmt.Println("No histories to log.")
		return
	}

	for _, history := range histories {
		fmt.Printf("History ID: %s\n", history.ID)
		fmt.Printf("User ID: %s\n", history.UserID)
		fmt.Printf("Drug ID: %s\n", history.DrugID)
		fmt.Printf("Group ID: %v\n", history.GroupID) // Will print <nil> if GroupID is nil
		fmt.Printf("Status: %s\n", history.Status)
		fmt.Printf("Taken At: %v\n", history.TakenAt) // Will print <nil> if TakenAt is nil
		fmt.Printf("Notified At: %s\n", history.NotifiedAt.Format(time.RFC3339))
		fmt.Printf("Count: %d\n", history.Count)
		fmt.Println("-----------------------------------")
	}
}

func LogAppointment(appointments []models.Appointment) {
	if len(appointments) == 0 {
		fmt.Println("No appointments to log.")
		return
	}

	for _, appointment := range appointments {
		fmt.Printf("Appointment ID: %s\n", appointment.ID)
		fmt.Printf("User ID: %s\n", appointment.UserID)
		fmt.Printf("Title: %s\n", appointment.Title)
		fmt.Printf("Datetime: %v\n", appointment.Datetime)
		fmt.Println("-----------------------------------")
	}
}