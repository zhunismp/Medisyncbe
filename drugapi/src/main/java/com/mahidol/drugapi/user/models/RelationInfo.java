package com.mahidol.drugapi.user.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RelationInfo {
    private UUID id;
    private UUID relativeId;
    private String firstName;
    private String lastName;
    private LocalDateTime createAt;
}
