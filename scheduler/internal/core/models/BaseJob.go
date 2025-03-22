package models

import "time"

type BaseJob interface {
	JobAttributes() JobAttributes
	Task(start time.Time)
}

type JobAttributes struct {
	Name     string
	Interval string
}
