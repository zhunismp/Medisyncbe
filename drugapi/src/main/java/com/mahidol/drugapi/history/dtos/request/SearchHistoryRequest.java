package com.mahidol.drugapi.history.dtos.request;

import com.mahidol.drugapi.common.models.Pagination;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class SearchHistoryRequest {
    @Min(1)
    @Max(12)
    private Integer month;
    private Integer year;
    private LocalDate preferredDate;
    private UUID referenceId; // Either drugId or groupId
    private Pagination pagination;

    public Optional<LocalDate> getPreferredDate() {
        return Optional.ofNullable(preferredDate);
    }

    public Optional<UUID> getReferenceId() {
        return Optional.ofNullable(referenceId);
    }

    public Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

    @AssertTrue(message = "Year must not be greater than the current year")
    public boolean isYearValid() {
        if (year == null) {
            return true;
        }
        int currentYear = Year.now().getValue();
        return year <= currentYear;
    }

    @AssertTrue(message = "Preferred date cannot be in the future")
    public boolean checkPreferredDateValidity() {
        if (preferredDate == null) {
            return true;
        }
        return !preferredDate.isAfter(LocalDate.now());
    }
}
