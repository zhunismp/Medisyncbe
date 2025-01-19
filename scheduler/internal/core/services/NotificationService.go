package services

import (
	"context"
	"fmt"
	"log"

	"google.golang.org/api/option"
	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/messaging"
)

type NotificationService struct {
	client *messaging.Client
}

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
	message := &messaging.Message{
		Topic: topic,
		Notification: &messaging.Notification{
			Title: title,
			Body:  body,
		},
		APNS: &messaging.APNSConfig{
            Headers: map[string]string{
                "apns-priority": "10",
            },
            Payload: &messaging.APNSPayload{
                Aps: &messaging.Aps{
                    Alert: &messaging.ApsAlert{
                        Title: title,
                        Body:  body,
                    },
                    Sound: "default",
                },
            },
        },
	}

	response, err := ns.client.Send(context.Background(), message)
	if err != nil {
		return fmt.Errorf("error sending FCM message: %v", err)
	}

	log.Printf("Successfully sent message to topic %s: %s", topic, response)
	return nil
}