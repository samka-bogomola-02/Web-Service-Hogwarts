package ru.hogwarts.school.interfaces;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyServiceInterface {
    //  CRUD-методы
    Faculty addFaculty(Faculty faculty);

    Faculty findFacultyById(long id);

    Faculty editFaculty(Faculty facultyForUpdate);

    Faculty deleteFaculty(long id);

    Collection<Faculty> getAllFaculty();
    List<Faculty> getByColor(String color);

    Faculty findByName(String name);

    Collection<Faculty> findByColor(String color);

    Collection<Faculty> findByNamePart(String part);

    String getLongestFacultyName();
}
