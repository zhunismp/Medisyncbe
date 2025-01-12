package com.mahidol.drugapi.user.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private MultipartFile profileImage;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Double weight;
    private Double height;
    private Character gender;
    private String bloodGroup;
    private List<String> healthCondition;
    private List<String> drugAllergy;
    private List<String> foodAllergy;

    public Optional<MultipartFile> getProfileImage() {
        return Optional.ofNullable(profileImage);
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public Optional<LocalDate> getBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public Optional<Double> getWeight() {
        return Optional.ofNullable(weight);
    }

    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    public Optional<Character> getGender() {
        return Optional.ofNullable(gender);
    }

    public Optional<String> getBloodGroup() {
        return Optional.ofNullable(bloodGroup);
    }

    public Optional<List<String>> getHealthCondition() {
        return Optional.ofNullable(healthCondition);
    }

    public Optional<List<String>> getDrugAllergy() {
        return Optional.ofNullable(drugAllergy);
    }

    public Optional<List<String>> getFoodAllergy() {
        return Optional.ofNullable(foodAllergy);
    }
}
