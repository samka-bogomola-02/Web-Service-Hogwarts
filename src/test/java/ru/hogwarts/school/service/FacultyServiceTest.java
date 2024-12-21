package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    private Faculty faculty;
    private Faculty faculty1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faculty = new Faculty(1L, "Griffindor", "blue");
        faculty1 = new Faculty(2L,"Slytherin", "green");
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

    @Test
    void testFindByName() {
        when(facultyRepository.findByNameIgnoreCase("Griffindor")).thenReturn(faculty);

        Faculty result = facultyService.findByName("Griffindor");

        assertNotNull(result);
        assertEquals(faculty, result);
        verify(facultyRepository).findByNameIgnoreCase("Griffindor");
    }

    @Test
    void testFindByNameNotFound() {
        when(facultyRepository.findByNameIgnoreCase("NonExistent")).thenReturn(null);

        Faculty result = facultyService.findByName("NonExistent");

        assertNull(result);
        verify(facultyRepository).findByNameIgnoreCase("NonExistent");
    }

    @Test
    void testFindByColor() {
        when(facultyRepository.findByColorContainsIgnoreCase("blue")).thenReturn(Arrays.asList(faculty));

        Collection<Faculty> result = facultyService.findByColor("blue");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(faculty));
        verify(facultyRepository).findByColorContainsIgnoreCase("blue");
    }

    @Test
    void testFindByColorNotFound() {
        when(facultyRepository.findByColorContainsIgnoreCase("yellow")).thenReturn(Collections.emptyList());

        Collection<Faculty> result = facultyService.findByColor("yellow");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(facultyRepository).findByColorContainsIgnoreCase("yellow");
    }

    @Test
    void testFindByNamePart() {
        when(facultyRepository.findByNameContainsIgnoreCase("Griff")).thenReturn(Arrays.asList(faculty));

        Collection<Faculty> result = facultyService.findByNamePart("Griff");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(faculty));
        verify(facultyRepository).findByNameContainsIgnoreCase("Griff");
    }

    @Test
    void testFindByNamePartNotFound() {
        when(facultyRepository.findByNameContainsIgnoreCase("NonExistent")).thenReturn(Collections.emptyList());

        Collection<Faculty> result = facultyService.findByNamePart("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(facultyRepository).findByNameContainsIgnoreCase("NonExistent");
    }

    @Test
    void testGetLongestFacultyName() {
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(faculty, faculty1));

        String result = facultyService.getLongestFacultyName();

        assertEquals("Griffindor", result);
        verify(facultyRepository).findAll();
    }

    @Test
    void testGetLongestFacultyNameNoFaculties() {
        when(facultyRepository.findAll()).thenReturn(Collections.emptyList());

        String result = facultyService.getLongestFacultyName();
        assertEquals("Нет факультетов", result); // Должен вернуть сообщение о том, что нет факультетов
        verify(facultyRepository).findAll();
    }
}