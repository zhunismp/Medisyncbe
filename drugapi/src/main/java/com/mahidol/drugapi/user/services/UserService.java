package com.mahidol.drugapi.user.services;

import com.mahidol.drugapi.common.utils.StringUtil;
import com.mahidol.drugapi.external.aws.s3.S3Service;
import com.mahidol.drugapi.user.dtos.requests.CreateUserRequest;
import com.mahidol.drugapi.user.dtos.requests.UpdateUserRequest;
import com.mahidol.drugapi.user.dtos.responses.GetUserResponse;
import com.mahidol.drugapi.user.models.entities.User;
import com.mahidol.drugapi.user.models.types.BloodGroup;
import com.mahidol.drugapi.user.models.types.Gender;
import com.mahidol.drugapi.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public UserService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    public String createUser(CreateUserRequest request) {
        User savedUser = userRepository.save(new User()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName().orElse(null))
                .setBirthDate(request.getBirthDate())
                .setWeight(request.getWeight().orElse(null))
                .setHeight(request.getHeight().orElse(null))
                .setGender(Gender.fromValue(request.getGender()))
                .setBloodGroup(BloodGroup.fromValue(request.getBloodGroup()))
                .setHealthCondition(StringUtil.arrayToString(request.getHealthCondition().orElse(null)))
                .setDrugAllergy(StringUtil.arrayToString(request.getDrugAllergy().orElse(null)))
        );

        // Upload user profile to s3 bucket
        request.getProfileImage().map(file -> s3Service.uploadFile("medisync-user-profile", savedUser.getId().toString(), file));

        return savedUser.getId().toString();
    }

    public GetUserResponse getUser(UUID userId) {
        return userRepository.findById(userId).map(user -> {
                    Optional<String> url = s3Service.getUrl("medisync-user-profile", userId.toString());
                    return GetUserResponse.fromUser(user, url);
                })
                .orElseThrow(() -> new EntityNotFoundException("User id not found with id: " + userId));
    }

    public void updateUser(UpdateUserRequest request) {
        userRepository.findById(request.getUserId()).map(user ->
                userRepository.save(
                        user
                                .setFirstName(request.getFirstName().orElse(user.getFirstName()))
                                .setLastName(request.getLastName().orElse(user.getLastName()))
                                .setBirthDate(request.getBirthDate().orElse(user.getBirthDate()))
                                .setWeight(request.getWeight().orElse(user.getWeight()))
                                .setHeight(request.getHeight().orElse(user.getHeight()))
                                .setGender(request.getGender().map(Gender::fromValue).orElse(user.getGender()))
                                .setBloodGroup(request.getBloodGroup().map(BloodGroup::fromValue).orElse(user.getBloodGroup()))
                                .setHealthCondition(request.getHealthCondition().map(StringUtil::arrayToString).orElse(user.getHealthCondition()))
                                .setDrugAllergy(request.getDrugAllergy().map(StringUtil::arrayToString).orElse(user.getDrugAllergy()))
                ));

        request.getProfileImage().map(file -> s3Service.uploadFile("medisync-user-profile", request.getUserId().toString(), file));
    }
}
