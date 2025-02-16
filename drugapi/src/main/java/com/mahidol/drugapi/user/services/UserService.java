package com.mahidol.drugapi.user.services;

import com.mahidol.drugapi.user.dtos.requests.*;
import com.mahidol.drugapi.user.dtos.responses.GetRelationResponse;
import com.mahidol.drugapi.user.dtos.responses.GetUserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Boolean isExists(UUID userId);
    void createUser(CreateUserRequest request);
    GetUserResponse getUser(Optional<UUID> relativeId);
    void updateUser(UpdateUserRequest request);
    void setUpRegisterToken(String token);

    GetRelationResponse getUserRelations();
    void addFriend(AddFriendRequest request);
    void removeRelation(RemoveRelationRequest request);
    void acceptFriend(AcceptFriendRequest request);
}
