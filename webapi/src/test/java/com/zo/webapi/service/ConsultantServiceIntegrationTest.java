package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ConsultantDTO;
import com.zo.webapi.dto.ConsultantResponseDTO;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.model.Manager;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ConsultantRepository;
import com.zo.webapi.repository.ManagerRepository;
import com.zo.webapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = WebapiApplication.class)
public class ConsultantServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private User consultantUser1;
    private User consultantUser2;
    private User consultantUser3;
    private Manager manager;


    @BeforeEach
    void setup() {
        consultantRepository.deleteAll();
        managerRepository.deleteAll();
        userRepository.deleteAll();


        //Skapa användarkonton
        consultantUser1 = userRepository.save(new User(null, "sturep", "password", "Sture P.", "sturep@mail.se", "Stureplan", UserRole.CONSULTANT));
        consultantUser2 = userRepository.save(new User(null, "gustaf", "password", "Gustaf", "gustaf@mail.se", "Göteborg", UserRole.CONSULTANT));
        consultantUser3 = userRepository.save(new User(null, "peter", "password", "Peter", "peter@mail.se", "Göteborg", UserRole.CONSULTANT));
        User managerUser = userRepository.save(new User(null, "supermackan", "password", "Markus", "supermackan@mail.se", "Malmö", UserRole.MANAGER));

        //Skapa en manager
        manager = managerRepository.save(new Manager(managerUser));
    }

    @Test
    void searchForConsultantUsingName_Success() {
        //Arrange
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser1.getId(), manager.getId()));
        consultantService.createConsultant(new ConsultantDTO(null,consultantUser2.getId(), manager.getId()));
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser3.getId(), manager.getId()));

        //Act
        List<ConsultantResponseDTO> foundConsultants = consultantService.searchConsultants("Stu", null, null);

        //Assert
        assertThat(foundConsultants).hasSize(1);
        assertEquals("Sture P.", foundConsultants.getFirst().getName());
    }

    @Test
    void FilterConsultantsUsingCity_Success() {
        //Arrange
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser1.getId(), manager.getId()));
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser2.getId(), manager.getId()));
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser3.getId(), manager.getId()));

        //Act
        List<ConsultantResponseDTO> foundConsultants = consultantService.searchConsultants(null, "Göteborg", null);


        //Assert
        assertThat(foundConsultants).hasSize(2);
        assertThat(foundConsultants).allMatch(c -> c.getCity().equals("Göteborg"));
    }

    @Test
    void FilterConsultantsUsingManagerId_Success() {
        //Arrange
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser1.getId(), manager.getId()));
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser2.getId(), manager.getId()));
        consultantService.createConsultant(new ConsultantDTO(null, consultantUser3.getId(), manager.getId()));

        Long managerId = managerRepository.findAll().getFirst().getId();

        //Act
        List<ConsultantResponseDTO> foundConsultants = consultantService.searchConsultants(null, null, managerId);

        //Assert
        assertThat(foundConsultants).hasSize(3);
        assertThat(foundConsultants).allMatch(c -> c.getManagerId().equals(managerId));
    }

    @Test
    void searchForConsultantUsingName_NoFound() {
        List<ConsultantResponseDTO> foundConsultants = consultantService.searchConsultants("Erik", null, null);
        assertThat(foundConsultants).isEmpty();
    }

}
