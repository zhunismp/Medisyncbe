package com.mahidol.drugapi.history.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GroupHistory {
    private UUID groupId;
    private String groupName;
    private List<DrugHistory> drugs;
}
