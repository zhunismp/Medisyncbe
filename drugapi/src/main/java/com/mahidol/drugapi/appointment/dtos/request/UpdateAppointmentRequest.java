package com.mahidol.drugapi.appointment.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UpdateAppointmentRequest {
    @NotNull(message = "appointment id should not be null")
    private UUID appointmentId;
    private String title;
    private String medicName;

    @NotNull(message = "datetime is mandatory")
    @Future(message = "datetime must not be in the past")
    private LocalDateTime datetime;
    private String destination;
    private String remark;

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<String> getMedicName() {
        return Optional.ofNullable(medicName);
    }

    public Optional<LocalDateTime> getDateTime() {
        return Optional.ofNullable(datetime);
    }

    public Optional<String> getDestination() {
        return Optional.ofNullable(destination);
    }

    public Optional<String> getRemark() {
        return Optional.ofNullable(remark);
    }

}
