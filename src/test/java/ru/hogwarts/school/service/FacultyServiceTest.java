package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faculty = new Faculty(1L, "Griffindor", "blue"); // Предположим, что у вас есть конструктор с параметрами
    }

    @Test
    void addFaculty() {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty savedFaculty = facultyService.addFaculty(faculty);

        assertNotNull(savedFaculty);
        assertEquals("Griffindor", savedFaculty.getName());
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    void findFacultyById() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyNotFoundException.class, () -> facultyService.findFacultyById(1L));
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void editFaculty() {
        when(facultyRepository.existsById(1L)).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty updatedFaculty = facultyService.editFaculty(faculty);

        assertNotNull(updatedFaculty);
        assertEquals("Griffindor", updatedFaculty.getName());
        verify(facultyRepository, times(1)).existsById(1L);
        verify(facultyRepository, times(1)).save(faculty);
    }
    @Test
    void editFacultyException() {
        when(facultyRepository.existsById(1L)).thenReturn(false);

        assertThrows(FacultyNotFoundException.class, () -> facultyService.editFaculty(faculty));
        verify(facultyRepository, times(1)).existsById(1L);
    }

    @Test
    void deleteFaculty() {when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        Faculty deletedFaculty = facultyService.deleteFaculty(1L);

        assertNotNull(deletedFaculty);
        assertEquals("Griffindor", deletedFaculty.getName());
        verify(facultyRepository, times(1)).deleteById(1L);

    }

    @Test
    void deleteFacultyException() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyNotFoundException.class, () -> facultyService.deleteFaculty(1L));
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void getAllFaculty() {
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(faculty));

        Collection<Faculty> faculties = facultyService.getAllFaculty();
        assertNotNull(faculties);
        assertEquals(1, faculties.size());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void getByColor() {
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(
                new Faculty(1L, "Griffindor", "Blue"),
                new Faculty(2L, "Sliserin", "Red")
        ));

        List<Faculty> faculties = facultyService.getByColor("Blue");

        assertNotNull(faculties);
        assertEquals(1, faculties.size());
        assertEquals("Griffindor", faculties.get(0).getName());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void getByColorWhenNoFacultiesMatchColor() {
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(
                new Faculty(1L, "Griffindor", "Blue"),
                new Faculty(2L, "Sliserin", "Red")
        ));
        List<Faculty> faculties = facultyService.getByColor("Green");

        assertNotNull(faculties);
        assertTrue(faculties.isEmpty());
        verify(facultyRepository, times(1)).findAll();
    }
}