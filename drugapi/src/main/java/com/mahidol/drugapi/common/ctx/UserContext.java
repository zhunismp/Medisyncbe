package com.mahidol.drugapi.common.ctx;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestScope
@Component
@Data
public class UserContext {
    private UUID userId;
}
