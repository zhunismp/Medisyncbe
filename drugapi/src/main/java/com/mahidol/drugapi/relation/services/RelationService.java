package com.mahidol.drugapi.relation.services;

import com.mahidol.drugapi.relation.models.RelationResponse;

import java.util.UUID;

public interface RelationService {
    RelationResponse get();
    void unfriend(UUID relationId);

    void pending(UUID relativeId);
    void accept(UUID relationId, String relation, Boolean notifiable, Boolean readable);
    void reject(UUID requestId);
    void unpending(UUID requestId);
}
