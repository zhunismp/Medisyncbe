package com.mahidol.drugapi.common.middleware;

import com.mahidol.drugapi.common.ctx.Permission;
import com.mahidol.drugapi.common.utils.JWTUtil;
import com.mahidol.drugapi.common.ctx.UserContext;
import com.mahidol.drugapi.relation.services.RelationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserContext userContext;
    private final RelationService relationService;

    public JWTFilter(
            JWTUtil jwtUtil,
            UserContext userContext,
            RelationService relationService
    ) {
        this.jwtUtil = jwtUtil;
        this.userContext = userContext;
        this.relationService = relationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // TODO: Remove before go live
        String skipJwtHeader = request.getHeader("X-Skip-JWT");

        if (skipJwtHeader != null && skipJwtHeader.equalsIgnoreCase("true")) {
            logger.info("Skipping JWT validation due to X-Skip-JWT header");

            UUID mockUserId = UUID.fromString("a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68");
            userContext.setUserId(mockUserId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    mockUserId,
                    null,
                    Collections.emptyList()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1].trim();
            try {
                if (jwtUtil.isTokenValid(token)) {
                    UUID userId = UUID.fromString(jwtUtil.extractClaim(token, "userId"));
                    userContext.setUserId(userId);
                    List<Permission> permissions = relationService.get().getFriends().stream()
                            .map(r -> new Permission(r.getUserId(), r.getNotifiable(), r.getReadable())).toList();
                    userContext.setPermissions(permissions);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.emptyList()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                logger.error("JWT validation failed: ", ex);
            }
        }

        filterChain.doFilter(request, response);
    }
}
