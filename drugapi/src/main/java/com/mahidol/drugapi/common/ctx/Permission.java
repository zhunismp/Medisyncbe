package com.mahidol.drugapi.common.ctx;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Permission {
    private UUID userId;
    private Boolean notifiable;
    private Boolean readable;
}
