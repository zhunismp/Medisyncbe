package models

type Notification struct {
	Title string
	Body  string
	RegisterToken string
}

type NotificationTopic string

const (
	Drug NotificationTopic = "DRUG"
	Appointment NotificationTopic = "APPOINTMENT"
)

func (n NotificationTopic) String() string {
	return string(n)
}