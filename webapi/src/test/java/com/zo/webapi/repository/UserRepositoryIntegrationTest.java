package com.zo.webapi.repository;

import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real Postgres
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save user and find by username")
    void testSaveUserAndFindByUsername() {
        User user = new User(null, "alice", "password123", "alicesmith", UserRole.MANAGER);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("alice");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("alice");
        assertThat(foundUser.get().getRole()).isEqualTo(UserRole.MANAGER);
    }

    @Test
    @DisplayName("Check if username exists")
    void testCheckIfUsernameExists() {
        User user = new User(null, "john", "password123", "johndoe", UserRole.CONSULTANT);
        userRepository.save(user);

        assertThat(userRepository.existsByUsername("john")).isTrue();
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    @DisplayName("Save multiple users and retrieve all")
    void testSaveMultipleUsers() {
        User user1 = new User(null, "carol", "pass1", "Carol White", UserRole.CONSULTANT);
        User user2 = new User(null, "dave", "pass2", "Dave Green", UserRole.MANAGER);

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).extracting(User::getUsername).containsExactlyInAnyOrder("carol", "dave");

    }

    @Test
    @DisplayName("Delete a user")
    void testDeleteUser() {
        User user = new User(null, "Ben", "pass1", "Ben Green", UserRole.CONSULTANT);
        userRepository.save(user);
        userRepository.delete(user);

        assertThat(userRepository.existsByUsername("Ben")).isFalse();
    }

    @Test
    @DisplayName("Find by username returns empty if user does not exist")
    void testFindByUsername_NotFound() {
        Optional<User> user = userRepository.findByUsername("nonexistent");

        assertThat(user).isNotPresent();
    }

    @Test
    @DisplayName("Ensure default role is Consultant")
    void testDefaultRole() {
        User user = new User(null, "phoebe", "phoebes234", "Phoebe Green", null);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("phoebe");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getRole()).isEqualTo(UserRole.CONSULTANT);
    }

}
