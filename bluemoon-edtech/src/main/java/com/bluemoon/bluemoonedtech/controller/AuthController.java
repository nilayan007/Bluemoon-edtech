package com.bluemoon.bluemoonedtech.controller;

import com.bluemoon.bluemoonedtech.dto.RegisterRequest;
import com.bluemoon.bluemoonedtech.dto.UserResponse;
import com.bluemoon.bluemoonedtech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.ok(user);
    }
}
