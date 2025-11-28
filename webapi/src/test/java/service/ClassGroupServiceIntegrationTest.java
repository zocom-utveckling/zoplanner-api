package service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class ClassGroupServiceIntegrationTest {

    @Autowired
    private ClassGroupRepository classGroupRepository;
    @Autowired
    private ClassGroupService classGroupService;

    private ClassGroup existingClass;

    @BeforeEach
    public void setup() {
        classGroupRepository.deleteAll();

        existingClass = new ClassGroup("Java", 1L);
        existingClass = classGroupService.createClass(existingClass);
    }

    @Test
    void testCreateClass_Success() {
        ClassGroup newClass = new ClassGroup("Testing", 2L);
        ClassGroup saved = classGroupService.createClass(newClass);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Testing");
        assertThat(saved.getCustomerId()).isEqualTo(2L);
    }

    @Test
    void testGetClassById_Success() {
        Optional<ClassGroup> found =  classGroupRepository.findById(existingClass.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(existingClass.getName());
    }

    @Test
    void testUpdateClass_NotFound() {
        ClassGroup update = new ClassGroup("Java OOP", 3L);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                classGroupService.updateClass(999L, update));

        assertThat(exception.getMessage()).isEqualTo("Class not found with id: 999");

    }

    @Test
    void testDeleteClass_NotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> classGroupService.deleteClass(999L));
        assertThat(exception.getMessage()).isEqualTo("Class not found with id: 999");
    }
}
*/