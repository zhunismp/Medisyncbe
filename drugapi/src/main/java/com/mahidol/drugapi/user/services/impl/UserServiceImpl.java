package com.mahidol.drugapi.user.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.mahidol.drugapi.common.utils.StringUtil;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.external.aws.s3.S3Service;
import com.mahidol.drugapi.relation.models.RelationResponse;
import com.mahidol.drugapi.relation.models.entities.Relation;
import com.mahidol.drugapi.relation.models.entities.RelationRequested;
import com.mahidol.drugapi.relation.services.RelationService;
import com.mahidol.drugapi.user.dtos.requests.*;
import com.mahidol.drugapi.user.dtos.responses.GetRelationResponse;
import com.mahidol.drugapi.user.dtos.responses.GetUserResponse;
import com.mahidol.drugapi.user.models.RelationInfo;
import com.mahidol.drugapi.user.models.RelationRequestedInfo;
import com.mahidol.drugapi.user.models.entities.User;
import com.mahidol.drugapi.user.models.types.BloodGroup;
import com.mahidol.drugapi.user.models.types.Gender;
import com.mahidol.drugapi.user.repositories.UserRepository;
import com.mahidol.drugapi.user.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RelationService relationService;
    private final S3Service s3Service;
    private final UserContext userContext;
    private final FirebaseMessaging firebaseMessaging;

    public UserServiceImpl(
            UserRepository userRepository,
            RelationService relationService,
            S3Service s3Service,
            UserContext userContext,
            FirebaseMessaging firebaseMessaging
    ) {
        this.userRepository = userRepository;
        this.relationService = relationService;
        this.s3Service = s3Service;
        this.userContext = userContext;
        this.firebaseMessaging = firebaseMessaging;
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
        return userRepository.findById(userContext.getUserId()).map(user -> GetUserResponse.fromUser(user, Optional.empty()))
                .orElseThrow(() -> new EntityNotFoundException("User id not found with id: " + userContext.getUserId()));
    }

    public GetUserResponse getUser(UUID relativeId) {
        // Check is user already friend or not
        // Wrong usage
        relationService.getIncomingPermission(relativeId);

        return userRepository.findById(relativeId).map(user -> GetUserResponse.fromUser(user, Optional.empty()))
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

    public void setUpRegisterToken(String token) {
        Message dummy = Message.builder()
                .setToken(token)
                .build();

        try {
            firebaseMessaging.send(dummy, true);
            User u = userRepository.getReferenceById(userContext.getUserId());

            userRepository.save(u.setRegisterToken(token));
        } catch (FirebaseMessagingException ex) {
            logger.error("Error from cloud messaging: " + ex.getMessage());
            throw new IllegalArgumentException("Firebase thing go wrong");
        }
    }

    public GetRelationResponse getUserRelations() {
        RelationResponse relations = relationService.get();
        List<RelationInfo> friends = relations.getFriends().stream().map(this::transformRelationInfo).toList();
        List<RelationRequestedInfo> pending = relations.getPending().stream().map(r -> transformRelationRequestedInfo(r, false)).toList();
        List<RelationRequestedInfo> requested = relations.getRequested().stream().map(r -> transformRelationRequestedInfo(r, true)).toList();

        return new GetRelationResponse()
                .setFriends(friends)
                .setPending(pending)
                .setRequested(requested);
    }

    @Override
    public void addFriend(AddFriendRequest request) {
        if (!userRepository.existsById(request.getRelativeId()))
            throw new IllegalArgumentException("Friend doesn't exists with id: " + request.getRelativeId().toString());

        relationService.pending(request.getRelativeId());
    }

    @Override
    public void removeFriend(RemoveRelationRequest request) {
        relationService.unfriend(request.getRelativeId());
    }

    @Override
    public void acceptFriend(AcceptFriendRequest request) {
        relationService.accept(request.getRequestId(), request.getRelation(), request.getNotifiable(), request.getReadable());
    }

    @Override
    public void rejectFriend(RejectFriendRequest request) {
        relationService.reject(request.getRequestId());
    }

    @Override
    public void updateFriend(UpdateFriendRequest request) {
        relationService.update(
                request.getRelativeId(),
                request.getRelation(),
                request.getNotifiable(),
                request.getReadable()
        );
    }

    private RelationRequestedInfo transformRelationRequestedInfo(RelationRequested r, Boolean isRequested) {
        UUID targetUserId = isRequested ? r.getUserId() : r.getRelativeId();
        User relative = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User id does not exist"));

        return new RelationRequestedInfo(
            r.getId(),
            r.getRelativeId(),
            relative.getFirstName(),
            relative.getLastName(),
            r.getCreateAt()
        );
    }

    private RelationInfo transformRelationInfo(Relation r) {
        UUID targetId = r.getUserId().equals(userContext.getUserId()) ? r.getRelativeId() : r.getUserId();
        User relative = userRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("User id does not exists"));

        return new RelationInfo(
                r.getId(),
                r.getRelativeId(),
                relative.getFirstName(),
                relative.getLastName(),
                r.getNotifiable(),
                r.getReadable(),
                r.getCreateAt()
        );
    }
}
