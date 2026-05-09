package com.tutoring.userservice.controller;

import com.tutoring.userservice.dto.*;
import com.tutoring.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }
}
