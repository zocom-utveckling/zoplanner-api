package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ClassGroupResponseDTO;
import com.zo.webapi.dto.CreateClassGroupRequestDTO;
import com.zo.webapi.dto.UpdateClassGroupRequestDTO;
import com.zo.webapi.model.Customer;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class ClassGroupServiceIntegrationTest {

    @Autowired
    private ClassGroupService classGroupService;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setup() {
        classGroupRepository.deleteAll();
        customerRepository.deleteAll();

        customer1 = new Customer();
        customer1.setName("Customer A");
        customer1.setCity("Stockholm");
        customer1.setManager(null);
        customer1 = customerRepository.save(customer1);

        customer2 = new Customer();
        customer2.setName("Customer B");
        customer2.setCity("Göteborg");
        customer2.setManager(null);
        customer2 = customerRepository.save(customer2);
    }

    @Test
    void testCreateClass_Success() {
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO("Java Class", customer1.getId());

        ClassGroupResponseDTO created = classGroupService.createClass(requestDTO);

        assertNotNull(created.getId());
        assertEquals("Java Class", created.getName());
        assertEquals(customer1.getId(), created.getCustomerId());
        assertEquals("Customer A", created.getCustomerName());

        assertTrue(classGroupRepository.existsById(created.getId()));
    }

    @Test
    void testCreateClass_CustomerNotFound() {
        long missingCustomerId = 9999L;
        CreateClassGroupRequestDTO requestDTO = new CreateClassGroupRequestDTO("Java Class", missingCustomerId);

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.createClass(requestDTO));

        assertTrue(exc.getMessage().contains("Customer not found with id"));
    }

    @Test
    void testCreateClass_DuplicateNameSameCustomer_Conflict() {
        classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer1.getId()));

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer1.getId())));

        assertTrue(exc.getMessage().contains("409"));
        assertTrue(exc.getMessage().toLowerCase().contains("already exists"));
    }

    @Test
    void testCreateClass_SameNameDifferentCustomer_Ok() {
        classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer1.getId()));

        ClassGroupResponseDTO created2 =
                classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer2.getId()));

        assertNotNull(created2.getId());
        assertEquals(customer2.getId(), created2.getCustomerId());
    }

    @Test
    void testGetAllClasses() {
        classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer1.getId()));
        classGroupService.createClass(new CreateClassGroupRequestDTO("Testing Class", customer1.getId()));

        List<ClassGroupResponseDTO> allClasses = classGroupService.getAllClasses();

        assertEquals(2, allClasses.size());
        assertTrue(allClasses.stream().anyMatch(c -> c.getName().equals("Java Class")));
        assertTrue(allClasses.stream().anyMatch(c -> c.getName().equals("Testing Class")));
    }

    @Test
    void testGetClassById_Success() {
        ClassGroupResponseDTO created = classGroupService.createClass
                (new CreateClassGroupRequestDTO("Java Class", customer1.getId()));

       ClassGroupResponseDTO found = classGroupService.getClassById(created.getId());

       assertEquals(created.getId(), found.getId());
       assertEquals("Java Class", found.getName());
       assertEquals(customer1.getId(), found.getCustomerId());
    }

    @Test
    void testGetClassById_NotFound() {
        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.getClassById(9999L));

        assertTrue(exc.getMessage().contains("404"));
        assertTrue(exc.getMessage().contains("Class not found"));
    }

    @Test
    void testGetClassesByCustomerId_Success() {
        classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer1.getId()));
        classGroupService.createClass(new CreateClassGroupRequestDTO("Testing Class", customer1.getId()));

        List<ClassGroupResponseDTO> classList = classGroupService.getClassesByCustomerId(customer1.getId());

        assertEquals(2, classList.size());
        assertTrue(classList.stream().allMatch(c -> c.getCustomerId().equals(customer1.getId())));
    }

    @Test
    void testGetClassesByCustomerId_CustomerNotFound() {
        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.getClassesByCustomerId(9999L));

        assertTrue(exc.getMessage().contains("404"));
        assertTrue(exc.getMessage().contains("Customer not found"));
    }

    @Test
    void testUpdateClass_Success() {
        ClassGroupResponseDTO created = classGroupService.createClass
                (new CreateClassGroupRequestDTO("Java Class", customer1.getId()));

        UpdateClassGroupRequestDTO update = new UpdateClassGroupRequestDTO("Updated Name");

        ClassGroupResponseDTO updated = classGroupService.updateClass(created.getId(), update);

        assertEquals(created.getId(), updated.getId());
        assertEquals("Updated Name", updated.getName());

        assertTrue(classGroupRepository.existsById(created.getId()));
    }

    @Test
    void testUpdateClass_NotFound() {
        UpdateClassGroupRequestDTO update = new UpdateClassGroupRequestDTO("Updated Name");

        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.updateClass(9999L, update));

        assertTrue(exc.getMessage().contains("404"));
        assertTrue(exc.getMessage().contains("Class not found"));
    }

    @Test
    void testDeleteClass_Success() {
        ClassGroupResponseDTO created =
                classGroupService.createClass(new CreateClassGroupRequestDTO("Java Class", customer1.getId()));

        assertTrue(classGroupRepository.existsById(created.getId()));
        classGroupService.deleteClass(created.getId());
        assertFalse(classGroupRepository.existsById(created.getId()));
    }

    @Test
    void testDeleteClass_NotFound(){
        ResponseStatusException exc = assertThrows(ResponseStatusException.class,
                () -> classGroupService.deleteClass(9999L));

        assertTrue(exc.getMessage().contains("404"));
        assertTrue(exc.getMessage().contains("Class not found"));
    }
}