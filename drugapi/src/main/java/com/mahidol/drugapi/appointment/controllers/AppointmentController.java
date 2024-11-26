package com.mahidol.drugapi.appointment.controllers;

import com.mahidol.drugapi.appointment.dtos.request.CreateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.SearchAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.request.UpdateAppointmentRequest;
import com.mahidol.drugapi.appointment.dtos.response.SearchAppointmentResponse;
import com.mahidol.drugapi.appointment.services.AppointmentService;
import com.mahidol.drugapi.common.exceptions.BindingError;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/searchappointments")
    public ResponseEntity<?> searchAppointment(@RequestBody @Valid SearchAppointmentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        SearchAppointmentResponse response = appointmentService.searchAppointment(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@RequestBody @Valid CreateAppointmentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        appointmentService.createAppointment(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/appointments")
    public ResponseEntity<?> updateAppointment(@RequestBody @Valid UpdateAppointmentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        appointmentService.updateAppointment(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelAppointment(
            @RequestParam @NotNull(message = "userId must not be null") UUID userId,
            @RequestParam @NotNull(message = "appointment must not be null") UUID appointmentId
    ) {
        appointmentService.cancelAppointment(userId, appointmentId);

        return ResponseEntity.noContent().build();
    }
}
