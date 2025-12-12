package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;



@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class ClassGroupServiceIntegrationTest {

    @Autowired private ClassGroupService classGroupService;
    @Autowired private ClassGroupRepository classGroupRepository;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private UserRepository userRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setUsername("manager1");
        user.setPassword("pass");
        user.setName("Manager One");
        user.setRole(UserRole.MANAGER);
        userRepository.save(user);

        customer = new Customer();
        customer.setName("Customer A");
        customer.setCity("City X");
        customerRepository.save(customer);

    }


    @Test
    void testCreateClassGroup() {
        CreateClassGroupRequestDTO dto = new CreateClassGroupRequestDTO();
        dto.setName("Class 1");
        dto.setCustomerId(customer.getId());

        ClassGroupResponseDTO response = classGroupService.createClass(dto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Class 1");
        assertThat(response.getCustomerId()).isEqualTo(customer.getId());
    }

    @Test
    void testCreateClassGroupDuplicateName() {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setName("Class 1");
        classGroup.setCustomer(customer);
        classGroupRepository.save(classGroup);


        CreateClassGroupRequestDTO dto = new CreateClassGroupRequestDTO();
        dto.setName("Class 1");
        dto.setCustomerId(customer.getId());

        assertThatThrownBy(() -> classGroupService.createClass(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Class name already exists for this customer");

    }


    @Test
    void testGetClassesByCustomerId() {
        ClassGroup c1 = new ClassGroup("Class 1", customer);
        ClassGroup c2 = new ClassGroup("Class 2", customer);
        classGroupRepository.save(c1);
        classGroupRepository.save(c2);

        List<ClassGroupResponseDTO> classes = classGroupService.getClassesByCustomerId(customer.getId());

        assertThat(classes).hasSize(2);
    }

    @Test
    void testDeleteClassGroup() {
        ClassGroup classGroup = new ClassGroup("Class 1", customer);
        classGroupRepository.save(classGroup);

        classGroupService.deleteClass(classGroup.getId());

        assertThat(classGroupRepository.existsById(classGroup.getId())).isFalse();
    }

}

