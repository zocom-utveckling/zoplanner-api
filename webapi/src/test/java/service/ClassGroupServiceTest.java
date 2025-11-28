package service;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.repository.ClassGroupRepository;
import com.zo.webapi.service.ClassGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/*public class ClassGroupServiceTest {
    @Mock
    private ClassGroupRepository classGroupRepository;

    @InjectMocks
    private ClassGroupService classGroupService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetAllClass() {
        when(classGroupRepository.findAll()).thenReturn(List.of(new ClassGroup()));
        assertEquals(1, classGroupService.getAllClass().size());

    }

    @Test
    void testGetClassById() {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(10L);

        when(classGroupRepository.findById(10L)).thenReturn(Optional.of(classGroup));
        assertTrue(classGroupService.getClassById(10L).isPresent());
    }

    @Test
    void testGetClassById_NotFound() {
        //Arrange
        when(classGroupRepository.findById(99L)).thenReturn(Optional.empty());

        //Act
        Optional<ClassGroup> result = classGroupService.getClassById(99L);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty Optional when class not found");
        verify(classGroupRepository, times(1)).findById(99L);

    }


    @Test
    void testCreateClass() {
        ClassGroup newClass = new ClassGroup();
        when(classGroupRepository.save(newClass)).thenReturn(newClass);
        assertNotNull(classGroupService.createClass(newClass));
    }

    @Test
    void testDeleteClass() {
        when(classGroupRepository.existsById(1L)).thenReturn(true);
        classGroupService.deleteClass(1L);
        verify(classGroupRepository, times(1)).deleteById(1L);

    }

    @Test
    void testDeleteClass_NotFound() {
        when(classGroupRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            classGroupService.deleteClass(99L);
        });

        assertEquals("Class not found with id: 99", exception.getMessage());
    }
}
*/