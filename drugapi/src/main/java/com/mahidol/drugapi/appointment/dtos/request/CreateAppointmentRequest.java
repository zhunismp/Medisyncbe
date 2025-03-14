package com.mahidol.drugapi.appointment.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class CreateAppointmentRequest {
    @NotBlank(message = "title is mandatory")
    private String title;

    private String medicName;

    @NotNull(message = "datetime is mandatory")
    @Future(message = "datetime must not be in the past")
    private LocalDateTime datetime;

    private String destination;

    private String remark;
}
