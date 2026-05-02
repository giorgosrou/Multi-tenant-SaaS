package com.gero.saas_platform.user.service;

import com.gero.saas_platform.auth.dto.LoginRequest;
import com.gero.saas_platform.auth.dto.LoginResponse;
import com.gero.saas_platform.auth.service.JwtService;
import com.gero.saas_platform.user.dto.RegisterRequest;
import com.gero.saas_platform.user.model.Role;
import com.gero.saas_platform.user.model.User;
import com.gero.saas_platform.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = buildUser(request);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(token);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(token);
    }

    private User buildUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
    }

    public void updateRole(String userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setRole(role);
        userRepository.save(user);
    }

}
