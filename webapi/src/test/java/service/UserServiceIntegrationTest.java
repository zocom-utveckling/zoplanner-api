package service;


import com.zo.webapi.WebapiApplication;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private User user;


    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("jane");
        user.setPassword("password");
        user.setRole(UserRole.MANAGER);
        user.setName("Jane Smith");
        userRepository.save(user);


    }


    @Test
    void testCreateUser() {
        User newUser  = new User();
        newUser.setUsername("rachelgreen");
        newUser.setPassword("password");
        newUser.setRole(UserRole.CONSULTANT);
        newUser.setName("Rachel Green");

        User saved = userService.createUser(newUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("rachelgreen");

    }

    @Test
    void testUpdateUser() {
        User update =  new User();
        update.setUsername("updated");
        update.setPassword("password");
        update.setRole(UserRole.MANAGER);
        update.setName("Updated Name");

        User updated = userService.updateUser(user.getId(), update);

        assertThat(updated.getUsername()).isEqualTo("updated");
        assertThat(updated.getRole()).isEqualTo(UserRole.MANAGER);


    }

    @Test
    void testDeleteUserNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));

        assertThat(exception.getMessage()).isEqualTo("User not found with id: 999");
    }

    @Test
    void testDeleteConsultantUser() {
        User conUser = new User();
        conUser.setUsername("alex");
        conUser.setPassword("password");
        conUser.setRole(UserRole.CONSULTANT);
        conUser.setName("Alex Smith");
        userRepository.save(conUser);

        Consultant consultant = new Consultant();
        consultant.setCity("New York");
        consultant.setUser(conUser);
        consultantRepository.save(consultant);

        userService.deleteUser(conUser.getId());

        assertThat(userRepository.existsById(conUser.getId())).isFalse();
        assertThat(consultantRepository.findByUserId(conUser.getId())).isEmpty();

    }

}

