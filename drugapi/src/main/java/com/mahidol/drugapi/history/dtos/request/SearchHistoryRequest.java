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
    @Min(value = 1, message = "Please specify valid month")
    @Max(value = 12, message = "Please specify valid month")
    private Integer month;
    private Integer year;
    @Min(value = 1, message = "Please specify valid date")
    @Max(value = 31, message = "Please specify valid date")
    private Integer preferredDate;
    private UUID referenceId; // Either drugId or groupId
    private Pagination pagination;

    public Optional<Integer> getPreferredDate() {
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

    @AssertTrue(message = "Provided date cannot be in the future")
    public boolean checkDateValidity() {
        if (year == null || month == null || preferredDate == null) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDayOfMonth = currentDate.getDayOfMonth();

        if (year > currentYear) return false;
        if (year == currentYear && month > currentMonth) return false;


        // If the year and month are the same as the current year and month, check the day
        return year != currentYear || month != currentMonth || preferredDate <= currentDayOfMonth;
    }
}
