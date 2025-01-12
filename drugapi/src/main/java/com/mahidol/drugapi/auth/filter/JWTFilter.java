package com.mahidol.drugapi.auth.filter;

import com.mahidol.drugapi.auth.utils.JWTUtil;
import com.mahidol.drugapi.ctx.UserContext;
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

    public JWTFilter(JWTUtil jwtUtil, UserContext userContext) {
        this.jwtUtil = jwtUtil;
        this.userContext = userContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1].trim();
            try {
                if (jwtUtil.isTokenValid(token)) {
                    UUID userId = UUID.fromString(jwtUtil.extractClaim(token, "userId"));
                    userContext.setUserId(userId);
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
