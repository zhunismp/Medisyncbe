package com.mahidol.drugapi.user.dtos.responses;

import com.mahidol.drugapi.common.utils.StringUtil;
import com.mahidol.drugapi.user.models.entities.User;
import com.mahidol.drugapi.user.models.types.BloodGroup;
import com.mahidol.drugapi.user.models.types.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GetUserResponse {
    private String profileImage;

    private UUID id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Double weight;

    private Double height;

    private Gender gender;

    private BloodGroup bloodGroup;

    private List<String> healthCondition;

    private List<String> drugAllergy;

    public static GetUserResponse fromUser(User u, Optional<String> url) {
        return new GetUserResponse()
                .setProfileImage(url.orElse(null))
                .setId(u.getId())
                .setFirstName(u.getFirstName())
                .setLastName(u.getLastName())
                .setBirthDate(u.getBirthDate())
                .setWeight(u.getWeight())
                .setHeight(u.getHeight())
                .setGender(u.getGender())
                .setBloodGroup(u.getBloodGroup())
                .setHealthCondition(StringUtil.stringToArray(u.getHealthCondition()))
                .setDrugAllergy(StringUtil.stringToArray(u.getDrugAllergy()));
    }
}
