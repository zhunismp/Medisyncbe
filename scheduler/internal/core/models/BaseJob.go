package models

import "time"

type BaseJob interface {
	JobAttributes() JobAttributes
	Task(start time.Time, parameters ...interface{})
}

type JobAttributes struct {
	Name string
	Interval string
}