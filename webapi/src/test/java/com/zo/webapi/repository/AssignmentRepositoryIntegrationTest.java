package com.zo.webapi.repository;

import com.zo.webapi.model.Assignment;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AssignmentRepositoryIntegrationTest {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    void testFindByConsultantId() {
        Assignment assignment1 = new Assignment("Java OOP", 101L, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10), 1L);
        Assignment assignment2 = new Assignment("Web App", 102L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 10), 2L);

        assignmentRepository.save(assignment1);
        assignmentRepository.save(assignment2);

        List<Assignment> result = assignmentRepository.findByConsultantId(101L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseName()).isEqualTo("Java OOP");

    }

    @Test
    void testFindByCourseName() {
        Assignment assignment = new Assignment("Java OOP",  101L, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10), 1L);
        assignmentRepository.save(assignment);

        List<Assignment> result = assignmentRepository.findByCourseName("Java OOP");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConsultantId()).isEqualTo(101L);
    }

    @Test
    void testFindByDateStartBetween() {
        Assignment assignment1 = new Assignment(".NET", 101L, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10), 1L);
        Assignment assignment2 = new Assignment("Testing",  102L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 10), 2L);
        assignmentRepository.save(assignment1);
        assignmentRepository.save(assignment2);

        List<Assignment> result = assignmentRepository.findByDateStartBetween(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 20));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseName()).isEqualTo(".NET");
    }

    @Test
    void testFindByDateEndAfterNoMatch() {
        Assignment assignment = new Assignment("Testing", 101L, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10), 1L);
        assignmentRepository.save(assignment);

        List<Assignment> result = assignmentRepository.findByDateEndAfter(LocalDate.of(2025, 12, 1));
        assertThat(result).isEmpty();
    }

    @Test
    void testFindByClassIdNonExistent() {
        Assignment assignment = new Assignment("Testing", 101L, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10), 1L);
        assignmentRepository.save(assignment);

        List<Assignment> result = assignmentRepository.findByClassId(99L);
        assertThat(result).isEmpty();
    }
}
