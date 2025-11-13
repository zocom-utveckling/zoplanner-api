package com.zo.webapi.repository;

import com.zo.webapi.model.ClassGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClassGroupRepositoryIntegrationTest {
    @Autowired
    private ClassGroupRepository classGroupRepository;

    // Test variables
    private ClassGroup class1;
    private ClassGroup class2;
    private ClassGroup class3;

    @BeforeEach
    void setup() {
        // Clean up table before each test
        classGroupRepository.deleteAll();

        // Test objects
        class1 = new ClassGroup("Class 1", 100L);
        class2 = new ClassGroup("Class 2", 100L);
        class3 = new ClassGroup("Class 1", 200L);

        classGroupRepository.save(class1);
        classGroupRepository.save(class2);
        classGroupRepository.save(class3);

    }

    @Test
    void testFindByCustomerId_ReturnMatchingGroups() {
        // Act
        List<ClassGroup> classGroups = classGroupRepository.findByCustomerId(100L);

        // Assert
        assertThat(classGroups).hasSize(2);
        assertThat(classGroups).extracting(ClassGroup::getName).containsExactlyInAnyOrder("Class 1", "Class 2");

    }

    @Test
    void testFindByName_ReturnAllMatchingGroups() {
        // Act
        List<ClassGroup> classGroups = classGroupRepository.findByName("Class 1");

        // Assert
        assertThat(classGroups).hasSize(2);
        assertThat(classGroups).extracting(ClassGroup::getCustomerId).containsExactlyInAnyOrder(100L, 200L);

    }

    @Test
    void testSaveAndRetrieveClassGroups_Success() {
        // Arrange
        ClassGroup classGroup = new ClassGroup("Class 5", 300L);
        ClassGroup savedClassGroup = classGroupRepository.save(classGroup);

        // Act
        Optional<ClassGroup> found = classGroupRepository.findById(savedClassGroup.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Class 5");
        assertThat(found.get().getCustomerId()).isEqualTo(300L);
    }

    @Test
    void testFindByCustomerId_NotFound() {
        // Act
        Optional<ClassGroup> classGroup = classGroupRepository.findById(999L);

        // Assert
        assertThat(classGroup).isEmpty();
    }
}

