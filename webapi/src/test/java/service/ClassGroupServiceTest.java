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


public class ClassGroupServiceTest {
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
    void testCreateClass() {
        ClassGroup newClass = new ClassGroup();
        when(classGroupRepository.save(newClass)).thenReturn(newClass);
        assertNotNull(classGroupService.createClass(newClass));
    }
}
