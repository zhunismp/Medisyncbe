package com.mahidol.drugapi.relation.models;

import com.mahidol.drugapi.relation.models.entities.Relation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationResponse {
    private List<Relation> friends;
    private List<Relation> pending;
}
