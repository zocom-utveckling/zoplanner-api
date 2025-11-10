package com.zo.webapi.repository;

import com.zo.webapi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


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
}
