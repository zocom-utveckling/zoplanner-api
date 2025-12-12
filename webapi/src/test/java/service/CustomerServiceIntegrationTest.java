package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.CustomerCreateDTO;
import com.zo.webapi.dto.CustomerResponseDTO;
import com.zo.webapi.dto.CustomerUpdateDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Customer;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.CustomerRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import com.zo.webapi.service.CustomerService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CustomerServiceIntegrationTest {
   @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;

    private Manager manager;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setUsername("manager1");
        user.setPassword("password");
        user.setName("Manager one");
        user.setRole(UserRole.MANAGER);
        userRepository.save(user);

        manager = new Manager();
        manager.setUser(user);
        managerRepository.save(manager);

    }

    @Test
    void testCreateCustomer() {
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("Customer a");
        dto.setCity("city");
        dto.setManagerId(manager.getId());

        CustomerResponseDTO response = customerService.createCustomer(dto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Customer a");
        assertThat(response.getManager()).isNotNull();
        assertThat(response.getManager().getId()).isEqualTo(manager.getId());


    }

    @Test
    void testGetAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Customer 1");
        customer1.setCity("city");
        customer1.setManager(manager);
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Customer 2");
        customer2.setCity("city");
        customerRepository.save(customer2);

        List<Customer> customers = customerService.getAllCustomers();
        assertThat(customers).hasSize(2);

    }

    @Test
    void testDeleteCustomer() {
        Customer customer1 = new Customer();
        customer1.setName("Customer 1");
        customerRepository.save(customer1);

        customerService.deleteCustomer(customer1.getId());

        assertThat(customerRepository.existsById(customer1.getId())).isFalse();

    }

    @Test
    void testCreateCustomer_ManagerNotExist() {
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("Customer 1");
        dto.setCity("city");
        dto.setManagerId(999L);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> customerService.createCustomer(dto));
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getReason()).isEqualTo("Manager not found with id: 999");


    }


}
