package com.mahidol.drugapi.relation.services;

import com.mahidol.drugapi.relation.models.RelationResponse;

import java.util.UUID;

public interface RelationService {
    RelationResponse getRelation(UUID userId);
    void pendingRequest(UUID userId, UUID referenceId);
    void acceptRequest(UUID userId, UUID referenceId);
    void removeRelation(UUID userId, UUID requestId);
}
