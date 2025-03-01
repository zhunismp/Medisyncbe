package com.mahidol.drugapi.user.services;

import com.mahidol.drugapi.user.dtos.requests.*;
import com.mahidol.drugapi.user.dtos.responses.GetRelationResponse;
import com.mahidol.drugapi.user.dtos.responses.GetUserResponse;

import java.util.UUID;

public interface UserService {
    Boolean isExists(UUID userId);
    void createUser(CreateUserRequest request);
    GetUserResponse getUser(UUID relativeId);
    GetUserResponse getUser();
    void updateUser(UpdateUserRequest request);
    void setUpRegisterToken(String token);

    GetRelationResponse getUserRelations();
    void addFriend(AddFriendRequest request);
    void unpending(UnpendingRequest request);
    void removeFriend(RemoveRelationRequest request);
    void acceptFriend(AcceptFriendRequest request);
    void rejectFriend(RejectFriendRequest request);
    void updateFriend(UpdateFriendRequest request);
}
