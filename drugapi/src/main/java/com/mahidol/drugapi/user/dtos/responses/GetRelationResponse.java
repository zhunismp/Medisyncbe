package com.mahidol.drugapi.user.dtos.responses;

import com.mahidol.drugapi.user.models.RelationInfo;
import com.mahidol.drugapi.user.models.RelationRequestedInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GetRelationResponse {
    List<RelationInfo> friends;
    List<RelationRequestedInfo> pending;
    List<RelationRequestedInfo> requested;
}
