package com.mahidol.drugapi.relation.services;

import com.mahidol.drugapi.relation.models.RelationResponse;

import java.util.UUID;

public interface RelationService {
    RelationResponse getRelation(UUID userId);
    void pendingRequest(UUID userId, UUID relativeId);
    void acceptRequest(UUID relationId);
    void removeRelation(UUID userId, UUID requestId);
}
