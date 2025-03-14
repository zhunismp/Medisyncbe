package com.mahidol.drugapi.appointment.dtos.request;

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
public class SearchAppointmentRequest {
    private UUID relativeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;

    public LocalDateTime getStartDate() { return Optional.ofNullable(startDate).orElse(LocalDateTime.now()); }
    public Optional<LocalDateTime> getEndDate() { return Optional.ofNullable(endDate); }

    public Optional<String> getTitle() { return Optional.ofNullable(title); }
    public Optional<UUID> getRelativeId() { return Optional.ofNullable(relativeId); }
}
