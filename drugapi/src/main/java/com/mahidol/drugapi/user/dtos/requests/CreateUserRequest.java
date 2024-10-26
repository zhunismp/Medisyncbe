package com.mahidol.drugapi.user.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private MultipartFile profileImage;
    @NotBlank(message = "firstname is mandatory")
    private String firstName;
    private String lastName;
    @NotNull(message = "birthdate is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Double weight;
    private Double height;
    @NotNull(message = "gender is mandatory")
    private char gender;
    @NotBlank(message = "blood group is mandatory")
    private String bloodGroup;
    private List<String> healthCondition;
    private List<String> drugAllergy;

    public Optional<MultipartFile> getProfileImage() {
        return Optional.ofNullable(profileImage);
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public Optional<Double> getWeight() {
        return Optional.ofNullable(weight);
    }

    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    public Optional<List<String>> getHealthCondition() {
        return Optional.ofNullable(healthCondition);
    }

    public Optional<List<String>> getDrugAllergy() {
        return Optional.ofNullable(drugAllergy);
    }
}
