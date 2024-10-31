package ru.hogwarts.school.interfaces;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface FacultyServiceInterface {
    //  CRUD-методы
    Faculty addFaculty(Faculty faculty);

    Faculty findFacultyById(long id);

    Faculty editFaculty(Faculty faculty);

    Faculty deleteFaculty(long id);

    Collection<Faculty> getAllFaculty();
    List<Faculty> getByColor(String color);
}
