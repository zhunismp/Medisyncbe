package com.mahidol.drugapi.relation.services.impl;

import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.relation.models.Permission;
import com.mahidol.drugapi.relation.models.RelationResponse;
import com.mahidol.drugapi.relation.models.entities.Relation;
import com.mahidol.drugapi.relation.models.entities.RelationRequested;
import com.mahidol.drugapi.relation.repositories.RelationRepository;
import com.mahidol.drugapi.relation.repositories.RelationRequestedRepository;
import com.mahidol.drugapi.relation.services.RelationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the RelationService that manages user relationships,
 * including adding friends, removing friends, handling pending friend requests,
 * and accepting/rejecting requests.
 * <p>
 * This service interacts with two repositories:
 * - RelationRepository: Stores confirmed friendships.
 * - RelationRequestedRepository: Stores pending friend requests.
 * </p>
 * The UserContext is used to retrieve the current user’s ID for all operations.
 */
@Service
public class RelationServiceImpl implements RelationService {

    private final RelationRepository relationRepository;
    private final RelationRequestedRepository relationRequestedRepository;
    private final UserContext userContext;

    public RelationServiceImpl(
            RelationRepository relationRepository,
            RelationRequestedRepository relationRequestedRepository,
            UserContext userContext
    ) {
        this.relationRepository = relationRepository;
        this.relationRequestedRepository = relationRequestedRepository;
        this.userContext = userContext;
    }

    /**
     * Retrieves the current user's relationships.
     * <p>
     * The response includes:
     * - Friends: Confirmed friendships.
     * - Pending: Requests the user has sent but are not yet accepted.
     * - Requested: Requests the user has received but not yet accepted.
     * </p>
     *
     * @return RelationResponse containing the user’s friends and pending requests.
     */
    @Override
    public RelationResponse get() {
        List<Relation> friends = getRelations();
        List<RelationRequested> pending = relationRequestedRepository.findByUserId(userContext.getUserId());
        List<RelationRequested> requested = relationRequestedRepository.findByRelativeId(userContext.getUserId());

        return new RelationResponse(friends, pending, requested);
    }

    @Override
    public Permission getIncomingPermission(UUID relativeId) {
        return relationRepository.findByUserIdAndRelativeId(relativeId, userContext.getUserId()).stream()
                .findFirst()
                .map(r -> new Permission(r.getNotifiable(), r.getReadable()))
                .orElseThrow(() -> new IllegalArgumentException("User aren't friend"));
    }

    @Override
    public Relation getFriend(UUID relativeId) {
        return getRelation(userContext.getUserId(), relativeId);
    }

    @Override
    public void update(UUID relativeId, String relation, Boolean notifiable, Boolean readable) {
        Relation r = getRelation(userContext.getUserId(), relativeId)
                .setRelation(relation)
                .setNotifiable(notifiable)
                .setReadable(readable);

        relationRepository.save(r);
    }

    /**
     * Removes a friend by deleting the relation.
     * <p>
     * The operation is only allowed if the current user initiated the friendship.
     * </p>
     *
     * @param relativeId The ID of the friend to remove.
     * @throws IllegalArgumentException If the relation does not exist or was not created by the user.
     */
    @Override
    public void unfriend(UUID relativeId) {
        Relation out = getRelation(userContext.getUserId(), relativeId);
        Relation in = getRelation(relativeId, userContext.getUserId());

        relationRepository.deleteAllById(List.of(out.getId(), in.getId()));
    }

    /**
     * Sends a friend request to another user.
     * <p>
     * Validations:
     * - The user cannot send duplicate requests.
     * - The user cannot send a request if they are already friends.
     * </p>
     *
     * @param relativeId The ID of the user to send the request to.
     * @throws IllegalArgumentException If the request is a duplicate or the users are already friends.
     */
    @Override
    public void pending(UUID relativeId) {
        if (relationRequestedRepository.existsByUserIdAndRelativeId(userContext.getUserId(), relativeId))
            throw new IllegalArgumentException("Cannot send duplicate request. Or your friend has sent a request to you.");

        if (relationRepository.existsByUserIdAndRelativeId(userContext.getUserId(), relativeId)) {
            throw new IllegalArgumentException("Users are already friends");
        }

        RelationRequested r = new RelationRequested()
                .setUserId(userContext.getUserId())
                .setRelativeId(relativeId)
                .setCreateAt(LocalDateTime.now());

        relationRequestedRepository.save(r);
    }

    /**
     * Accepts a pending friend request and establishes a confirmed friendship.
     * <p>
     * Steps:
     * - Validates that the request exists.
     * - Removes the pending request.
     * - Creates a new confirmed relation.
     * </p>
     *
     * @param requestId  The ID of the friend request.
     * @param relation    The type of relation.
     * @param notifiable  Whether the user should receive notifications from the friend.
     * @param readable    Whether the user can read information from the friend.
     * @throws IllegalArgumentException If the request does not exist.
     */
    @Override
    @Transactional
    public void accept(UUID requestId, String relation, Boolean notifiable, Boolean readable) {
        RelationRequested pendingRequest = relationRequestedRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request never been created"));

        relationRequestedRepository.deleteById(pendingRequest.getId());

        Relation outgoing = new Relation()
                .setUserId(pendingRequest.getRelativeId())
                .setRelativeId(pendingRequest.getUserId())
                .setCreateAt(LocalDateTime.now())
                .setRelation(relation)
                .setNotifiable(notifiable)
                .setReadable(readable);
        Relation incoming = new Relation()
                .setUserId(pendingRequest.getUserId())
                .setRelativeId(pendingRequest.getRelativeId())
                .setCreateAt(LocalDateTime.now())
                .setNotifiable(Relation.DEFAULT_NOTIFIABLE)
                .setReadable(Relation.DEFAULT_READABLE);

        relationRepository.saveAll(List.of(outgoing, incoming));
    }

    /**
     * Rejects a pending friend request.
     * <p>
     * This operation is only allowed if the request was sent to the current user.
     * </p>
     *
     * @param requestId The ID of the friend request to reject.
     * @throws IllegalArgumentException If the request does not exist or does not belong to the user.
     */
    @Override
    public void reject(UUID requestId) {
        RelationRequested r = relationRequestedRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request never been created"));

        if (!r.getRelativeId().equals(userContext.getUserId()))
            throw new IllegalArgumentException("Request isn't related to this user");

        relationRequestedRepository.deleteById(requestId);
    }

    /**
     * Cancels a sent friend request before it is accepted.
     * <p>
     * This operation is only allowed if the current user is the sender of the request.
     * </p>
     *
     * @param requestId The ID of the friend request to cancel.
     * @throws IllegalArgumentException If the request does not exist or does not belong to the user.
     */
    @Override
    public void unpending(UUID requestId) {
        RelationRequested r = relationRequestedRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request never been created"));

        if (!r.getUserId().equals(userContext.getUserId()))
            throw new IllegalArgumentException("Request isn't belong to this user");

        relationRequestedRepository.deleteById(requestId);
    }

    private List<Relation> getRelations() {
        return relationRepository.findByUserId(userContext.getUserId()).stream().toList();
    }

    private Relation getRelation(UUID userId, UUID relativeId) {
        return relationRepository.findByUserIdAndRelativeId(userId, relativeId)
                .stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Relation not found"));
    }
}
