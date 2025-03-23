package com.mahidol.drugapi.appointment.services.impl;

import com.mahidol.drugapi.appointment.dtos.request.CreateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.SearchAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.UpdateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.response.AppointmentDTO;
import com.mahidol.drugapi.appointment.dtos.response.SearchAppointmentResponse;
import com.mahidol.drugapi.appointment.models.entities.Appointment;
import com.mahidol.drugapi.appointment.repositories.AppointmentRepository;
import com.mahidol.drugapi.appointment.services.AppointmentService;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.relation.services.RelationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserContext userContext;
    private final RelationService relationService;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            UserContext userContext,
            RelationService relationService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.userContext = userContext;
        this.relationService = relationService;
    }

    @Override
    public SearchAppointmentResponse searchAppointment(SearchAppointmentRequest request) {
        UUID id = request.getRelativeId().map(i -> {
            if (!relationService.getIncomingPermission(i).getReadable())
                throw new IllegalArgumentException("Access denied from your friend");

            return i;
        }).orElse(userContext.getUserId());

        List<Appointment> boundedAppointment =
                request.getEndDate().map(end -> appointmentRepository.findByUserIdAndDatetimeBetween(id, request.getStartDate(), end))
                .orElse(appointmentRepository.findByUserIdAndDatetimeAfter(id, request.getStartDate()));

        List<Appointment> filteredByTitle =
                request.getTitle()
                        .map(title -> boundedAppointment.stream().filter(a -> a.getTitle().equals(title)).toList())
                        .orElse(boundedAppointment);

        List<AppointmentDTO> result = filteredByTitle.stream().map(AppointmentDTO::fromAppointment).toList();

        return new SearchAppointmentResponse(
                result,
                result.size()
        );
    }

    @Override
    public void createAppointment(CreateAppointmentRequest request) {
        Appointment target = new Appointment()
                .setUserId(userContext.getUserId())
                .setTitle(request.getTitle())
                .setMedicName(request.getMedicName())
                .setDatetime(request.getDatetime())
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
        if (!validateOwner(userContext.getUserId(), request.getAppointmentId()))
            throw new IllegalArgumentException("User is not the owner of the appointment");

        // TODO: this might not safe
        Appointment target = appointmentRepository.findById(request.getAppointmentId()).get();

        appointmentRepository.save(
                target
                        .setTitle(request.getTitle().orElse(target.getTitle()))
                        .setDatetime(request.getDateTime().orElse(target.getDatetime()))
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
