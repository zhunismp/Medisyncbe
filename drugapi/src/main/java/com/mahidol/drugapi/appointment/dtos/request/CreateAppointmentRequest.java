package com.mahidol.drugapi.appointment.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class CreateAppointmentRequest {
    @NotNull(message = "user id should not be null")
    private UUID userId;
    @NotBlank(message = "title is mandatory")
    private String title;
    private String medicName;
    @NotNull(message = "date must not be null")
    private LocalDate date;
    @NotNull(message = "time must not be null")
    private LocalTime time;
    private String destination;
    private String remark;
}
