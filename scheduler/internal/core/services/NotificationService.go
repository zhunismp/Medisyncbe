package services

import (
	"context"
	"fmt"
	"log"

	"firebase.google.com/go"
	"firebase.google.com/go/messaging"
	"google.golang.org/api/option"
)

type NotificationService struct {
	client *messaging.Client
}

// NewNotificationService initializes a new NotificationService.
func NewNotificationService(firebaseCredentialsPath string) (*NotificationService, error) {

	opt := option.WithCredentialsFile(firebaseCredentialsPath)
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		return nil, fmt.Errorf("error initializing Firebase app: %v", err)
	}

	client, err := app.Messaging(context.Background())
	if err != nil {
		return nil, fmt.Errorf("error getting messaging client: %v", err)
	}

	return &NotificationService{
		client: client,
	}, nil
}

func (ns *NotificationService) SendNotificationToTopic(topic string, title string, body string) error {
	// Create a message to send to the topic
	message := &messaging.Message{
		Topic: topic,
		Notification: &messaging.Notification{
			Title: title,
			Body:  body,
		},
	}

	response, err := ns.client.Send(context.Background(), message)
	if err != nil {
		return fmt.Errorf("error sending FCM message: %v", err)
	}

	log.Printf("Successfully sent message to topic %s: %s", topic, response)
	return nil
}