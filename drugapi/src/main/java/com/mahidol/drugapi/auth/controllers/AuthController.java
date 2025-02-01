package com.mahidol.drugapi.auth.controllers;

import com.mahidol.drugapi.auth.dtos.GoogleAuthRequest;
import com.mahidol.drugapi.auth.services.GoogleAuthService;
import com.mahidol.drugapi.common.utils.JWTUtil;
import com.mahidol.drugapi.user.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public AuthController(
            GoogleAuthService googleAuthService,
            UserService userService,
            JWTUtil jwtUtil
    ) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> googleAuth(@RequestBody GoogleAuthRequest request) {
        UUID userId = googleAuthService.verify(request.getIdToken());
        String token = jwtUtil.generateJWT(userId);

        return ResponseEntity.ok(Map.of("token", token, "isRegistered", userService.isExists(userId)));
    }
}
