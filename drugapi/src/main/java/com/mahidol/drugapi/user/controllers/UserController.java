package com.mahidol.drugapi.user.controllers;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.user.dtos.requests.CreateUserRequest;
import com.mahidol.drugapi.user.dtos.requests.UpdateUserRequest;
import com.mahidol.drugapi.user.dtos.responses.GetUserResponse;
import com.mahidol.drugapi.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users", consumes = "multipart/form-data")
    public ResponseEntity<?> createUser(@ModelAttribute @Valid CreateUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        userService.createUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // TODO: Find proper way to handle error and validation
    @GetMapping("/users")
    public ResponseEntity<?> getUser() {
        GetUserResponse response = userService.getUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/users", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUser(@ModelAttribute @Valid UpdateUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        userService.updateUser(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/token")
    public ResponseEntity<?> setUp(@RequestParam("registerToken") String RegisterToken) {
        userService.setUpRegisterToken(RegisterToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
