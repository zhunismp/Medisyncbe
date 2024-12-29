package com.mahidol.drugapi.appointment.services.impl;

import com.mahidol.drugapi.appointment.dtos.request.CreateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.SearchAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.UpdateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.response.AppointmentDTO;
import com.mahidol.drugapi.appointment.dtos.response.SearchAppointmentResponse;
import com.mahidol.drugapi.appointment.models.entities.Appointment;
import com.mahidol.drugapi.appointment.repositories.AppointmentRepository;
import com.mahidol.drugapi.appointment.services.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    @Override
    public SearchAppointmentResponse searchAppointment(SearchAppointmentRequest request) {
        boolean isFilter = request.getDate().isPresent() || request.getTitle().isPresent();
        List<Appointment> allAppointments = appointmentRepository.findByUserId(request.getUserId());
        List<Appointment> filteredAppointment = allAppointments;

        if (isFilter) {
            List<Appointment> appointmentsByDrug = request.getDate()
                    .map(date -> allAppointments.stream().filter(a -> a.getDate().equals(date)).toList()).orElse(Collections.emptyList());
            List<Appointment> appointmentsByTitle = request.getTitle()
                    .map(title -> allAppointments.stream().filter(a -> a.getTitle().equals(title)).toList()).orElse(Collections.emptyList());

            Set<Appointment> mergedAppointments = new HashSet<>();
            mergedAppointments.addAll(appointmentsByDrug);
            mergedAppointments.addAll(appointmentsByTitle);

            filteredAppointment = new ArrayList<>(mergedAppointments);
        }

        List<AppointmentDTO> appointmentDTOS = filteredAppointment.stream().map(AppointmentDTO::fromAppointment).toList();

        return new SearchAppointmentResponse(
                appointmentDTOS,
                appointmentDTOS.size()
        );
    }

    @Override
    public void createAppointment(CreateAppointmentRequest request) {
        Appointment target = new Appointment()
                .setUserId(request.getUserId())
                .setTitle(request.getTitle())
                .setMedicName(request.getMedicName())
                .setDate(request.getDate())
                .setTime(request.getTime())
                .setDestination(request.getDestination())
                .setRemark(request.getRemark());

        appointmentRepository.save(target);
    }

    @Override
    public void cancelAppointment(UUID userId, UUID appointmentId) {
        if (!appointmentRepository.existsById(appointmentId))
            throw new IllegalArgumentException("Appointment id: " + appointmentId + " doesn't exists");
        if (validateOwner(userId, appointmentId))
            throw new IllegalArgumentException("User is not the owner of the appointment");

        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public void updateAppointment(UpdateAppointmentRequest request) {
        if (!appointmentRepository.existsById(request.getAppointmentId()))
            throw new IllegalArgumentException("Appointment id: " + request.getAppointmentId() + " doesn't exists");
        if (!validateOwner(request.getUserId(), request.getAppointmentId()))
            throw new IllegalArgumentException("User is not the owner of the appointment");

        Appointment target = appointmentRepository.findById(request.getAppointmentId()).get();

        appointmentRepository.save(
                target
                        .setTitle(request.getTitle().orElse(target.getTitle()))
                        .setDate(request.getDate().orElse(target.getDate()))
                        .setTime(request.getTime().orElse(target.getTime()))
                        .setMedicName(request.getMedicName().orElse(target.getMedicName()))
                        .setDestination(request.getDestination().orElse(target.getDestination()))
                        .setRemark(request.getRemark().orElse(target.getRemark()))
        );
    }

    private Boolean validateOwner(UUID userId, UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(a -> a.getUserId().equals(userId))
                .orElse(false);
    }
}
