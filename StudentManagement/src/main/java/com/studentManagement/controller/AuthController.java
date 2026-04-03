package com.studentManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentManagement.dto.Dtos.ApiResponse;
import com.studentManagement.dto.Dtos.AuthResponse;
import com.studentManagement.dto.Dtos.LoginRequest;
import com.studentManagement.model.User;
import com.studentManagement.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        return ResponseEntity.ok(ApiResponse.success(
                "Login successful.",
                authService.login(request, httpRequest, httpResponse)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(ApiResponse.success("Logout successful.", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Current user fetched.", authService.getCurrentUser(user)));
    }
}
