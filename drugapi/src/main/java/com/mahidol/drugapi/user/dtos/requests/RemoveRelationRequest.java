package com.mahidol.drugapi.user.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveRelationRequest {
    private UUID relativeId;
}
