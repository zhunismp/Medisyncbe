package com.mahidol.drugapi.common.middleware;

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
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserContext userContext;

    public JWTFilter(
            JWTUtil jwtUtil,
            UserContext userContext
    ) {
        this.jwtUtil = jwtUtil;
        this.userContext = userContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1].trim();
            try {
                if (jwtUtil.isTokenValid(token)) {
                    UUID userId = UUID.fromString(jwtUtil.extractClaim(token, "userId"));
                    userContext.setUserId(userId);

                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList())
                    );
                }
            } catch (Exception ex) {
                sendErrorResponse(response, "JWT validation failed. Please ensure your credentials.", HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // Request without header will decline. Because context didn't set properly.
        // Request with allow endpoint will pass through.
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write("{\"errorMessage\": \"" + message + "\"}");
    }
}
