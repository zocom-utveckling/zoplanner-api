/*
package com.zo.webapi.repository;

import com.zo.webapi.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


import java.util.List;
import java.util.Optional;


@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername(){
        //Arrange test data
        User user = new User();
        user.setUsername("Ben");
        user.setPassword("123456");
        user.setRole("USER");
        user.setName("Ben Ten");
        user.setCity("London");
        userRepository.save(user);

        //Act
        //Call repo to find user --> return as optional (may or may not exist)
        Optional<User> foundUser = userRepository.findByUsername("Ben");

        //Assert : Check results are as expected
        assertThat(foundUser.isPresent());
        assertThat(foundUser.get().getUsername()).isEqualTo("Ben");
        assertThat(foundUser.get().getPassword()).isEqualTo("123456");
        assertThat(foundUser.get().getRole()).isEqualTo("USER");
        assertThat(foundUser.get().getName()).isEqualTo("Ben Ten");
        assertThat(foundUser.get().getCity()).isEqualTo("London");
    }

    @Test
    void testExistsByUsername(){
        // Arrange test data
        User user = new User();
        user.setUsername("Linda");
        user.setPassword("secret");
        user.setRole("ADMIN");
        user.setName("Bob Builder");
        userRepository.save(user);

        // Act
        //True for saved user
        boolean exists = userRepository.existsByUsername("Linda");
        //False for a username that doesn't exist
        boolean notExists = userRepository.existsByUsername("Bob Builder");

        // Assert : Check results
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testSaveAndGetAllUsers() {
        User user1 = new User(null, "User1", "pass1", "USER", "City1", "Name1");
        User user2 = new User(null, "User2", "pass2", "ADMIN", "City2", "Name2");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).extracting(User::getName).containsExactlyInAnyOrder("Name1", "Name2");


    }

    @Test
    void testUpdateUser() {
        User user = new User(null, "User1", "pass1", "USER", "City1", "Name1");
        userRepository.save(user);

        user.setPassword("newpass");
        user.setCity("newcity");
        userRepository.save(user);

        Optional<User> updatedUser = userRepository.findByUsername("User1");
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getPassword()).isEqualTo("newpass");
        assertThat(updatedUser.get().getCity()).isEqualTo("newcity");
    }

    @Test
    void testDeleteUser() {
        User user = new User(null, "User1", "pass1", "USER", "City1", "Name1");
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findByUsername("User1");
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void testDuplicateUsernameThrowsException() {
        User user1 = new User(null, "User1", "pass1", "USER", "City1", "Name1");
        User user2 = new User(null, "User1", "pass2", "ADMIN", "City2", "Name2");

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
