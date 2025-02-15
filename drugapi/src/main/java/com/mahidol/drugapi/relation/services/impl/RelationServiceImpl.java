package com.mahidol.drugapi.relation.services.impl;

import com.mahidol.drugapi.relation.models.RelationResponse;
import com.mahidol.drugapi.relation.models.entities.Relation;
import com.mahidol.drugapi.relation.models.types.Status;
import com.mahidol.drugapi.relation.repositories.RelationRepository;
import com.mahidol.drugapi.relation.services.RelationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class RelationServiceImpl implements RelationService {

    private final RelationRepository relationRepository;

    public RelationServiceImpl(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    @Override
    public RelationResponse getRelation(UUID userId) {
        // Search 2 ways A -> B and B -> A
        List<Relation> relations = Stream.concat(
                relationRepository.findByUserId(userId).stream(),
                relationRepository.findByRelativeId(userId).stream()
        ).toList();

        /*
            friends -> status is ACCEPTED
            pending -> userId == userId
            requested -> relativeId == userId
         */
        List<Relation> friends = relations.stream().filter(r -> (r.getStatus()).equals(Status.ACCEPTED)).toList();
        List<Relation> pending = relations.stream().filter(r -> (r.getStatus()).equals(Status.PENDING) && r.getUserId().equals(userId)).toList();
        List<Relation> requested = relations.stream().filter(r -> (r.getStatus()).equals(Status.PENDING) && r.getRelativeId().equals(userId)).toList();

        return new RelationResponse(
                friends,
                pending,
                requested
        );
    }

    @Override
    public void pendingRequest(UUID userId, UUID relativeId) {
        if (relationRepository.existsByUserIdAndRelativeId(userId, relativeId) || relationRepository.existsByUserIdAndRelativeId(relativeId, userId))
            throw new IllegalArgumentException("Can not send duplicate request");

        Relation r = new Relation()
                .setUserId(userId)
                .setRelativeId(relativeId)
                .setStatus(Status.PENDING)
                .setCreateAt(LocalDateTime.now());

        relationRepository.save(r);
    }

    @Override
    public void acceptRequest(UUID userId, UUID relativeId) {
        // check if request created or not
        if (!relationRepository.existsByUserIdAndRelativeId(userId, relativeId))
            throw new IllegalArgumentException("Request never been created");

        // remove pending request for both way A -> B , B -> A
        relationRepository.deleteByUserIdAndRelativeId(userId, relativeId);
        relationRepository.deleteByUserIdAndRelativeId(relativeId, userId);

        Relation r = new Relation()
                .setUserId(userId)
                .setRelativeId(relativeId)
                .setStatus(Status.ACCEPTED)
                .setCreateAt(LocalDateTime.now());

        relationRepository.save(r);
    }

    @Override
    public void removeRelation(UUID userId, UUID requestId) {
        Relation r = relationRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request never been created"));

        if (!r.getUserId().equals(userId))
            throw new IllegalArgumentException("Request isn't create by this user");

        relationRepository.delete(r);
    }
}
