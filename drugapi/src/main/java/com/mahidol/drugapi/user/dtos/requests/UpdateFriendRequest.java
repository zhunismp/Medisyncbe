package com.mahidol.drugapi.user.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFriendRequest {
    @NotNull
    private UUID relativeId;
    private String relation;
    private Boolean notifiable;
    private Boolean readable;
}
