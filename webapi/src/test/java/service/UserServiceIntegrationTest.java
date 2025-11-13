package service;


import com.zo.webapi.WebapiApplication;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User(null, "john_doe", "password123", "USER", "New York", "John Doe");
        userRepository.save(testUser);
    }


    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testGetUserByUsernameSuccess() {
        User user = userService.getUserByUsername("john_doe");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetUserById_NotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.getUserById(999L));

        assertTrue(exception.getMessage().contains("User not found with id"));
    }

    @Test
    void testUpdateUser_NotFound() {
        User updatedUser = new User(null, "jane_doe", "newpass", "ADMIN", "London", "Jane Doe");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(999L, updatedUser));
        assertTrue(exception.getMessage().contains("User not found with id"));
    }

}
