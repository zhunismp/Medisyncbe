package com.mahidol.drugapi.user.services;

import com.mahidol.drugapi.common.utils.StringUtil;
import com.mahidol.drugapi.common.ctx.UserContext;
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
    private final UserContext userContext;

    public UserService(
            UserRepository userRepository,
            S3Service s3Service,
            UserContext userContext
    ) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.userContext = userContext;
    }

    public Boolean isExists(UUID userId) {
        return userRepository.existsById(userId);
    }

    public void createUser(CreateUserRequest request) {
        if (userRepository.existsById(userContext.getUserId())) throw new IllegalArgumentException("User already exists.");

        User savedUser = userRepository.save(new User()
                .setId(userContext.getUserId())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName().orElse(null))
                .setBirthDate(request.getBirthDate())
                .setWeight(request.getWeight().orElse(null))
                .setHeight(request.getHeight().orElse(null))
                .setGender(Gender.fromValue(request.getGender()))
                .setBloodGroup(BloodGroup.fromValue(request.getBloodGroup()))
                .setHealthCondition(StringUtil.arrayToString(request.getHealthCondition().orElse(null)))
                .setDrugAllergy(StringUtil.arrayToString(request.getDrugAllergy().orElse(null)))
                .setFoodAllergy(StringUtil.arrayToString(request.getFoodAllergy().orElse(null)))
        );

        // Upload user profile to s3 bucket
        request.getProfileImage().map(file -> s3Service.uploadFile("medisync-user-profile", savedUser.getId().toString(), file));
    }

    public GetUserResponse getUser() {
        return userRepository.findById(userContext.getUserId()).map(user -> {
                    Optional<String> url = s3Service.getUrl("medisync-user-profile", userContext.getUserId().toString());
                    return GetUserResponse.fromUser(user, url);
                })
                .orElseThrow(() -> new EntityNotFoundException("User id not found with id: " + userContext.getUserId()));
    }

    public void updateUser(UpdateUserRequest request) {
        userRepository.findById(userContext.getUserId()).map(user ->
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
                                .setFoodAllergy(request.getFoodAllergy().map(StringUtil::arrayToString).orElse(user.getDrugAllergy()))
                )
        ).orElseThrow(() -> new EntityNotFoundException("User id does not exists."));

        request.getProfileImage().map(file -> s3Service.uploadFile("medisync-user-profile", userContext.getUserId().toString(), file));
    }
}
