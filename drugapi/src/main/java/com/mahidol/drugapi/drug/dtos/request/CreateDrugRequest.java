package com.mahidol.drugapi.drug.dtos.request;

import com.mahidol.drugapi.common.models.ScheduleTime;
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

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDrugRequest {

    private MultipartFile image;

    @NotBlank(message = "generic name should not be null")
    private String genericName;

    @NotBlank(message = "dosage form should not be null")
    private String dosageForm;

    @NotBlank(message = "unit should not be null")
    private String unit;

    @NotBlank(message = "strength should not be null")
    private String strength;

    @NotNull(message = "amount should not be null")
    @Min(value = 1, message = "Amount should greater than 0")
    private double amount;

    @NotNull(message = "dose should not be null")
    @Min(value = 1, message = "Dose should greater than 0")
    private double dose;

    @NotNull(message = "usage time should not be null")
    @Min(value = 1, message = "Invalid usage time")
    @Max(value = 3, message = "Invalid usage time")
    private int usageTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<ScheduleTime> scheduleTimes;

    private Boolean isInternalDrug;

    public Option<MultipartFile> getImage() {
        return Option.of(image);
    }
}
