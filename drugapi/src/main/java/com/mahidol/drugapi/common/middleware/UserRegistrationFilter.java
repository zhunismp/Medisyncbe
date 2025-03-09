package com.mahidol.drugapi.common.middleware;

import com.mahidol.drugapi.common.utils.JWTUtil;
import com.mahidol.drugapi.user.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/*
    Filter for verify user need to create user first, otherwise reject request.
 */
@Component
public class UserRegistrationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final List<String> EXCLUDED_PATH = List.of("/debug/health", "/api/v1/users");

    public UserRegistrationFilter(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDED_PATH.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // After passing JWT Filter it will guarantee to have Auth header
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1].trim();
        UUID userId = UUID.fromString(jwtUtil.extractClaim(token, "userId"));

        if (!userService.isExists(userId)) {
            String errorResp = "{\"errorMessage\": \"User not registered\"}";

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(errorResp);

            return;
        }

        filterChain.doFilter(request, response);
    }
}
