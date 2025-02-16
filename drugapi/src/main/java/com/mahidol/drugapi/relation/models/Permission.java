package com.mahidol.drugapi.relation.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private Boolean notifiable;
    private Boolean readable;
}
