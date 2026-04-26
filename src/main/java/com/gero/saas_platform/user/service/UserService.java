package com.gero.saas_platform.user.service;

import com.gero.saas_platform.auth.dto.LoginRequest;
import com.gero.saas_platform.auth.dto.LoginResponse;
import com.gero.saas_platform.user.dto.RegisterRequest;
import com.gero.saas_platform.user.dto.UserResponse;

public interface UserService {

    /**
     * Registers a new user in the system.
     * This method performs the following:
     *     Validates that the email is not already in use
     *     Creates a new {@link com.gero.saas_platform.user.model.User} entity
     *     Persists the user in the database
     *
     * @param request the registration request containing user credentials (email and password)
     * @return a {@link com.gero.saas_platform.user.dto.UserResponse} containing the created user's basic information
     * @throws RuntimeException if authentication if email already exists
     */
    UserResponse register(RegisterRequest request);

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     * This method performs the following:
     *     Retrieves the user by email
     *     Validates the provided password against the stored hashed password
     *     Generates a JWT token for authenticated access
     *
     * @param request the login request containing email and password
     * @return a {@link com.gero.saas_platform.auth.dto.LoginResponse} containing the JWT token
     * @throws RuntimeException if authentication fails due to invalid credentials
     */
    LoginResponse login(LoginRequest request);
}
