package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {
    @Test
    void addFaculty() {
        FacultyService facultyService = new FacultyService();

        Faculty faculty = new Faculty(0L, "Griffindor", "green"); // Добавляем первый факультет
        Faculty addedFaculty1 = facultyService.addFaculty(faculty);

        assertNotNull(addedFaculty1);// Проверяем, что студент был добавлен
        assertEquals("Griffindor", addedFaculty1.getName());
        assertEquals("green", addedFaculty1.getColor());
        assertEquals(1L, addedFaculty1.getId()); // Проверяем, что ID равен 1

        Faculty faculty2 = new Faculty(0L, "Sliserin", "yellow"); // Добавляем второго студента
        Faculty addedFaculty2 = facultyService.addFaculty(faculty2);

        assertNotNull(addedFaculty2); // Проверяем, что второй студент был добавлен
        assertEquals("Sliserin", addedFaculty2.getName());
        assertEquals("yellow", addedFaculty2.getColor());
        assertEquals(2L, addedFaculty2.getId()); // Проверяем, что ID равен 2

    }

    @Test
    void findFacultyById() {
        FacultyService facultyService = new FacultyService();
        Faculty faculty = new Faculty(0L, "Griffindor", "green"); // Добавляем первый факультет
        faculty.setName("Griff");
        faculty.setColor("green");
        facultyService.addFaculty(faculty);

        Faculty foundFaculty = facultyService.findFacultyById(1L);

        assertNotNull(foundFaculty); // Проверяем, что факультет был найден
        assertEquals("Griff", foundFaculty.getName());
    }

    @Test
    void editFaculty() {
        FacultyService facultyService = new FacultyService();
        Faculty faculty = new Faculty(1L, "Griffindor", "green");
        faculty.setName("Griffindor");
        faculty.setColor("green");
        facultyService.addFaculty(faculty);

        faculty.setName("Griff");
        faculty.setColor("green1");
        Faculty editedFaculty = facultyService.editFaculty(faculty);

        assertNotNull(editedFaculty); // Проверяем, что факультет был изменен
        assertEquals("Griff", editedFaculty.getName());
        assertEquals("green1", editedFaculty.getColor());
        // Проверка на несуществующий факультет
        Faculty nonExistingFaculty = new Faculty(2L, "NonExisting", "yellow");
        assertNull(facultyService.editFaculty(nonExistingFaculty)); // Проверяем, что результат равен null
    }

    @Test
    void deleteFaculty() {
        FacultyService facultyService = new FacultyService();
        Faculty faculty = new Faculty(1L, "Puffendui", "black");
        faculty.setName("but I don't like tests");
        faculty.setColor("gray");
        facultyService.addFaculty(faculty);
        facultyService.deleteFaculty(1);
        Faculty deletedFaculty = facultyService.findFacultyById(1);
        assertNull(deletedFaculty); // Проверяем, что факультет был удален
    }

    @Test
    void getAllFaculty() {
        FacultyService facultyService = new FacultyService();
        Faculty faculty1 = new Faculty(1L, "Griffindor", "green");
        Faculty faculty2 = new Faculty(2L, "Kohtevran", "white");
        facultyService.addFaculty(faculty1);
        facultyService.addFaculty(faculty2);

        Collection<Faculty> allFaculties = facultyService.getAllFaculty();

        assertNotNull(allFaculties);
        assertEquals(2, allFaculties.size());
        assertTrue(allFaculties.contains(faculty1));
        assertTrue(allFaculties.contains(faculty2));
    }

    @Test
    void getByColor() {
        FacultyService facultyService = new FacultyService();
        Faculty faculty1 = new Faculty(1L, "Griffindor", "green");
        Faculty faculty2 = new Faculty(2L, "Sliserin", "yellow");
        facultyService.addFaculty(faculty1);
        facultyService.addFaculty(faculty2);

        Collection<Faculty> greenFaculties = facultyService.getByColor("green");

        assertNotNull(greenFaculties);
        assertEquals(1, greenFaculties.size());
        assertTrue(greenFaculties.contains(faculty1));
        Collection<Faculty> yellowFaculties = facultyService.getByColor("yellow");

        assertNotNull(yellowFaculties);
        assertEquals(1, yellowFaculties.size());
        assertTrue(yellowFaculties.contains(faculty2));
        Collection<Faculty> nonExistingColorFaculties = facultyService.getByColor("red");

        assertNotNull(nonExistingColorFaculties);
        assertEquals(0, nonExistingColorFaculties.size());

    }
}