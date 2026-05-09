package com.tutoring.userservice.service;

import com.tutoring.userservice.dto.*;
import com.tutoring.userservice.model.User;
import com.tutoring.userservice.repository.UserRepository;
import com.tutoring.userservice.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private JwtUtil jwtUtil;

    private User activeUser;
    private User inactiveUser;
    private LoginRequest loginRequest;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "testSecretKeyForJWTTesting1234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000);
        ReflectionTestUtils.setField(userService, "jwtUtil", jwtUtil);

        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setEmail("test@example.com");
        activeUser.setPassword("encodedPassword");
        activeUser.setFirstName("John");
        activeUser.setLastName("Doe");
        activeUser.setPhoneNumber("1234567890");
        activeUser.setRole(User.Role.STUDENT);
        activeUser.setActive(true);

        inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("encodedPassword");
        inactiveUser.setFirstName("Jane");
        inactiveUser.setLastName("Doe");
        inactiveUser.setPhoneNumber("0987654321");
        inactiveUser.setRole(User.Role.TUTOR);
        inactiveUser.setActive(false);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("newpass123");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");
        signupRequest.setPhoneNumber("5555555555");
        signupRequest.setRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Should authenticate user with valid credentials")
    void authenticate_ValidCredentials_ReturnsJwtResponse() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // When
        JwtResponse response = userService.authenticate(loginRequest);

        // Then
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("STUDENT", response.getRole());
    }

    @Test
    @DisplayName("Should throw exception when user not found during authentication")
    void authenticate_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticate(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when password doesn't match")
    void authenticate_InvalidPassword_ThrowsException() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticate(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when account is deactivated")
    void authenticate_InactiveAccount_ThrowsException() {
        // Given
        loginRequest.setEmail("inactive@example.com");
        when(userRepository.findByEmail("inactive@example.com")).thenReturn(Optional.of(inactiveUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticate(loginRequest));
        assertEquals("Account is deactivated", exception.getMessage());
    }

    @Test
    @DisplayName("Should register new user successfully")
    void register_NewUser_ReturnsUserResponse() {
        // Given
        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setEmail("new@example.com");
        savedUser.setPassword("encodedNewPass");
        savedUser.setFirstName("New");
        savedUser.setLastName("User");
        savedUser.setPhoneNumber("5555555555");
        savedUser.setRole(User.Role.STUDENT);
        savedUser.setActive(true);

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newpass123")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserResponse response = userService.register(signupRequest);

        // Then
        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("new@example.com", response.getEmail());
        assertEquals("New User", response.getFullName());
        assertEquals("STUDENT", response.getRole());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void register_DuplicateEmail_ThrowsException() {
        // Given
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.register(signupRequest));
        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void getUserById_ExistingUser_ReturnsUserResponse() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));

        // When
        UserResponse response = userService.getUserById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void getUserById_NonExistingUser_ThrowsException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(999L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should get all users successfully")
    void getAllUsers_ReturnsListOfUserResponses() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setRole(User.Role.TUTOR);
        user2.setActive(true);

        when(userRepository.findAll()).thenReturn(Arrays.asList(activeUser, user2));

        // When
        List<UserResponse> responses = userService.getAllUsers();

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("test@example.com", responses.get(0).getEmail());
        assertEquals("user2@example.com", responses.get(1).getEmail());
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void deactivateUser_ExistingUser_DeactivatesUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));
        when(userRepository.save(any(User.class))).thenReturn(activeUser);

        // When
        userService.deactivateUser(1L);

        // Then
        assertFalse(activeUser.isActive());
        verify(userRepository).save(activeUser);
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existing user")
    void deactivateUser_NonExistingUser_ThrowsException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deactivateUser(999L));
        assertEquals("User not found", exception.getMessage());
    }
}
