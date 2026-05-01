package com.gero.saas_platform.user.controller;

import com.gero.saas_platform.auth.dto.LoginRequest;
import com.gero.saas_platform.auth.dto.LoginResponse;
import com.gero.saas_platform.user.dto.RegisterRequest;
import com.gero.saas_platform.user.dto.UserResponse;
import com.gero.saas_platform.user.model.Role;
import com.gero.saas_platform.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PatchMapping("/admin/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRole(@PathVariable String id, @RequestParam Role role) {
        userService.updateRole(id, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("You are authenticated!");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Admin area";
    }
}
