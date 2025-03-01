package com.mahidol.drugapi.relation.services;

import com.mahidol.drugapi.relation.models.Permission;
import com.mahidol.drugapi.relation.models.RelationResponse;
import com.mahidol.drugapi.relation.models.entities.Relation;

import java.util.UUID;

public interface RelationService {
    RelationResponse get();
    Permission getPermission(UUID relativeId);
    Relation getFriend(UUID relativeId);
    void update(UUID relativeId, String relation, Boolean notifiable, Boolean readable);
    void unfriend(UUID relativeId);

    void pending(UUID relativeId);
    void accept(UUID requestId, String relation, Boolean notifiable, Boolean readable);
    void reject(UUID requestId);
    void unpending(UUID requestId);
}
