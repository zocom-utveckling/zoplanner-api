
package com.zo.webapi.repository;

import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.List;
import java.util.Optional;


/*@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername(){
        //Arrange test data
        User user = new User();
        user.setUsername("Ben");
        user.setPassword("123456");
        user.setRole(UserRole.CONSULTANT);
        user.setName("Ben Ten");
        user.setEmail("benten@mail.com");
        user.setCity("London");
        userRepository.save(user);

        //Act
        //Call repo to find user --> return as optional (may or may not exist)
        Optional<User> foundUser = userRepository.findByUsername("Ben");

        //Assert : Check results are as expected
        assertThat(foundUser.isPresent());
        assertThat(foundUser.get().getUsername()).isEqualTo("Ben");
        assertThat(foundUser.get().getPassword()).isEqualTo("123456");
        assertThat(foundUser.get().getRole()).isEqualTo(UserRole.CONSULTANT);
        assertThat(foundUser.get().getName()).isEqualTo("Ben Ten");
        assertThat(foundUser.get().getCity()).isEqualTo("London");
        assertThat(foundUser.get().getEmail()).isEqualTo("benten@mail.com");

    }

    @Test
    void testExistsByUsername(){
        // Arrange test data
        User user = new User();
        user.setUsername("Linda");
        user.setPassword("secret");
        user.setRole(UserRole.CONSULTANT);
        user.setName("Bob Builder");
        user.setCity("Eslöv");
        user.setEmail("linda@mail.com");
        userRepository.save(user);

        // Act
        //True for saved user
        boolean exists = userRepository.existsByUsername("Linda");
        //False for a username that doesn't exist
        boolean notExists = userRepository.existsByUsername("Bob Builder");
        // Assert : Check results
        assertTrue(exists);
        assertFalse(notExists);

    }

    @Test
    void testSaveAndGetAllUsers() {
        User user1 = new User(null, "User1", "pass1", "John Doe", "City1", "user1@email.com", UserRole.MANAGER);
        User user2 = new User(null, "User2", "pass2", "Jane Doe", "City2", "user2@mail.com", UserRole.MANAGER);
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).extracting(User::getName).containsExactlyInAnyOrder("John Doe", "Jane Doe");


    }

    @Test
    void testUpdateUser() {
        User user = new User(null, "User1", "pass1", "USER", "City1", "user@mail.com", UserRole.CONSULTANT);
        userRepository.save(user);

        user.setPassword("newpass");
        user.setCity("newcity");
        user.setRole(UserRole.BOTH);
        userRepository.save(user);

        Optional<User> updatedUser = userRepository.findByUsername("User1");
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getPassword()).isEqualTo("newpass");
        assertThat(updatedUser.get().getCity()).isEqualTo("newcity");
        assertThat(updatedUser.get().getRole()).isEqualTo(UserRole.BOTH);

    }

    @Test
    void testDeleteUser() {
        User user = new User(null, "User1", "pass1", "USER", "user@email.com", "City1", UserRole.CONSULTANT);
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findByUsername("User1");
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void testDuplicateUsernameThrowsException() {
        User user1 = new User(null, "User1", "pass1", "USER", "user1@mail.com", "City1", UserRole.MANAGER);
        User user2 = new User(null, "User1", "pass1", "USER2", "user2@mail.com", "City1", UserRole.MANAGER);

        userRepository.save(user1);

        assertThatThrownBy(() -> userRepository.saveAndFlush(user2)).isInstanceOf(Exception.class);
    }

    @Test
    void testSaveUserWithMissingRequiredFields() {
        User user = new User();

        assertThatThrownBy(() -> userRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void testFindByUsernameNotFound() {
        Optional<User> foundUser = userRepository.findByUsername("notfound");
        assertThat(foundUser).isEmpty();
    }

}

 */