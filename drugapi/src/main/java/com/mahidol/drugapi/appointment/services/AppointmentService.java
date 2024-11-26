package com.mahidol.drugapi.appointment.services;

import com.mahidol.drugapi.appointment.dtos.request.CreateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.SearchAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.UpdateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.response.SearchAppointmentResponse;

import java.util.UUID;

public interface AppointmentService {
    SearchAppointmentResponse searchAppointment(SearchAppointmentRequest request);

    void createAppointment(CreateAppointmentRequest request);

    void cancelAppointment(UUID userId, UUID appointmentId);

    void updateAppointment(UpdateAppointmentRequest request);
}
