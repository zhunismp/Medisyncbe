package com.mahidol.drugapi.user.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptFriendRequest {
    @NotNull
    private UUID relationId;
    @Deprecated
    private String relation;
    @NotNull
    private Boolean notifiable;
    @NotNull
    private Boolean readable;
}
