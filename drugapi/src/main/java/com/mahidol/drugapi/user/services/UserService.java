package com.mahidol.drugapi.user.services;

import com.mahidol.drugapi.user.dtos.requests.AddFriendRequest;
import com.mahidol.drugapi.user.dtos.requests.CreateUserRequest;
import com.mahidol.drugapi.user.dtos.requests.UpdateUserRequest;
import com.mahidol.drugapi.user.dtos.responses.GetRelationResponse;
import com.mahidol.drugapi.user.dtos.responses.GetUserResponse;

import java.util.UUID;

public interface UserService {
    Boolean isExists(UUID userId);
    void createUser(CreateUserRequest request);
    GetUserResponse getUser();
    void updateUser(UpdateUserRequest request);
    void setUpRegisterToken(String token);

    GetRelationResponse getUserRelations();
    void addFriend(AddFriendRequest request);
}
