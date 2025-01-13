package com.mahidol.drugapi.user.models.entities;

import com.mahidol.drugapi.user.models.types.BloodGroup;
import com.mahidol.drugapi.user.models.types.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "app_user")
public class User {
    @Id
    private UUID id;
    @Column(name = "first_name", columnDefinition = "TEXT")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "TEXT")
    private String lastName;
    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;
    @Column(name = "weight", columnDefinition = "FLOAT")
    private Double weight;
    @Column(name = "height", columnDefinition = "FLOAT")
    private Double height;
    @Column(name = "gender", columnDefinition = "CHAR")
    private Gender gender;
    @Column(name = "blood_group", columnDefinition = "VARCHAR(5)")
    private BloodGroup bloodGroup;
    @Column(name = "health_condition", columnDefinition = "TEXT")
    private String healthCondition;
    @Column(name = "drug_allergy", columnDefinition = "TEXT")
    private String drugAllergy;
    @Column(name = "food_allergy", columnDefinition = "TEXT")
    private String foodAllergy;
}
