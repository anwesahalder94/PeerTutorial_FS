package com.tutoring.userservice.repository;

import com.tutoring.userservice.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find user by email when user exists")
    void findByEmail_ExistingUser_ReturnsUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");
        user.setRole(User.Role.STUDENT);
        user.setActive(true);
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void findByEmail_NonExistingUser_ReturnsEmpty() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should check if email exists")
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        // Given
        User user = new User();
        user.setEmail("exists@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");
        user.setRole(User.Role.STUDENT);
        user.setActive(true);
        entityManager.persist(user);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByEmail("exists@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should check if email does not exist")
    void existsByEmail_NonExistingEmail_ReturnsFalse() {
        // When
        boolean exists = userRepository.existsByEmail("notfound@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should save user correctly")
    void save_ValidUser_PersistsUser() {
        // Given
        User user = new User();
        user.setEmail("new@example.com");
        user.setPassword("password");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setPhoneNumber("0987654321");
        user.setRole(User.Role.TUTOR);
        user.setActive(true);

        // When
        User saved = userRepository.save(user);

        // Then
        assertNotNull(saved.getId());
        assertEquals("new@example.com", saved.getEmail());
    }

    @Test
    @DisplayName("Should find user by ID")
    void findById_ExistingUser_ReturnsUser() {
        // Given
        User user = new User();
        user.setEmail("findbyid@example.com");
        user.setPassword("password");
        user.setFirstName("Find");
        user.setLastName("ById");
        user.setPhoneNumber("1111111111");
        user.setRole(User.Role.ADMIN);
        user.setActive(true);
        User persisted = entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findById(persisted.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
    }

    @Test
    @DisplayName("Should find all users")
    void findAll_MultipleUsers_ReturnsAllUsers() {
        // Given
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPhoneNumber("1111111111");
        user1.setRole(User.Role.STUDENT);
        user1.setActive(true);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPhoneNumber("2222222222");
        user2.setRole(User.Role.TUTOR);
        user2.setActive(true);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // When
        var users = userRepository.findAll();

        // Then
        assertTrue(users.size() >= 2);
    }
}
