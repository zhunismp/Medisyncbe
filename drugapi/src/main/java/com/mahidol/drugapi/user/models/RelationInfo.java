package com.mahidol.drugapi.user.models;

import com.mahidol.drugapi.user.models.types.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RelationInfo {
    private UUID relativeId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private Boolean notifiable;
    private Boolean readable;
    private LocalDateTime createAt;
}
