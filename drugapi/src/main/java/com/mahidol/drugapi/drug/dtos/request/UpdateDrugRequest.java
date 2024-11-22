package com.mahidol.drugapi.drug.dtos.request;

import io.vavr.control.Option;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDrugRequest {
    @NotNull(message = "user id should not be null")
    private UUID userId;

    @NotBlank(message = "device id is mandatory")
    private String deviceId;

    private MultipartFile image;

    @NotNull(message = "drug id should not be null")
    private UUID drugId;

    private String genericName;

    private String dosageForm;

    private String unit;

    private String strength;

    @Min(value = 0, message = "Amount should greater than 0")
    private Double amount;

    @Min(value = 1, message = "Dose should greater than 0")
    private Double dose;

    @Min(value = 1, message = "Invalid usage time")
    @Max(value = 3, message = "Invalid usage time")
    private Integer usageTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<LocalTime> schedules;

    private Boolean isEnabled;

    public Option<MultipartFile> getImage() {
        return Option.of(image);
    }

    public Optional<String> getGenericName() {
        return Optional.ofNullable(genericName);
    }

    public Optional<String> getDosageForm() {
        return Optional.ofNullable(dosageForm);
    }

    public Optional<String> getUnit() {
        return Optional.ofNullable(unit);
    }

    public Optional<String> getStrength() {
        return Optional.ofNullable(strength);
    }

    public Optional<Double> getAmount() {
        return Optional.ofNullable(amount);
    }

    public Optional<Double> getDose() {
        return Optional.ofNullable(dose);
    }

    public Optional<Integer> getUsageTime() {
        return Optional.ofNullable(usageTime);
    }

    public Optional<List<LocalTime>> getSchedules() {
        return Optional.ofNullable(schedules);
    }

    public Optional<Boolean> getIsEnabled() {
        return Optional.ofNullable(isEnabled);
    }

}
