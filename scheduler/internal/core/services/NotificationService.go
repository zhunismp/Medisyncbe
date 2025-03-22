package services

import (
	"context"
	"fmt"
	"log"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/messaging"
	"github.com/zhunismp/Medisyncbe/scheduler/internal/core/models"
	"google.golang.org/api/option"
)

type NotificationService struct {
	client *messaging.Client
}

func NewNotificationService(opt option.ClientOption) (*NotificationService, error) {

	// TODO: Remove hardcoded project ID
	app, err := firebase.NewApp(context.Background(), &firebase.Config{
		ProjectID: "mdbe-79bbd",
	}, opt)
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

func (ns *NotificationService) SendNotification(token string, topic models.NotificationTopic, title string, body string) error {
	message := &messaging.Message{
		Token: token,
		Notification: &messaging.Notification{
			Title: title,
			Body:  body,
		},
		Data: map[string]string{
			"topic": topic.String(),
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

	log.Printf("Successfully sent message: %s", response)
	return nil
}
