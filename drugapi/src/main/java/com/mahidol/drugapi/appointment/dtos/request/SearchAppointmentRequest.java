package com.mahidol.drugapi.appointment.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class SearchAppointmentRequest {
    private UUID relativeId;
    private LocalDate date;
    private String title;

    public Optional<LocalDate> getDate() { return Optional.ofNullable(date); }
    public Optional<String> getTitle() { return Optional.ofNullable(title); }
    public Optional<UUID> getRelativeId() { return Optional.ofNullable(relativeId); }
}
