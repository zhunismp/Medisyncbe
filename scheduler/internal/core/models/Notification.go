package models

type Notification struct {
	Title         string
	Body          string
	RegisterToken string
}

type NotificationTopic string

const (
	DrugTopic        NotificationTopic = "DRUG"
	AppointmentTopic NotificationTopic = "APPOINTMENT"
	FamilyTopic     NotificationTopic = "FAMILY"
)

func (n NotificationTopic) String() string {
	return string(n)
}
