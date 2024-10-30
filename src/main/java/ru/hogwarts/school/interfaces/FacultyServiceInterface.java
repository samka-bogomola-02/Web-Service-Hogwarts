package ru.hogwarts.school.interfaces;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyServiceInterface {
    //  CRUD-методы
    Faculty addFaculty(Faculty faculty);

    Faculty findFacultyById(long id);

    Faculty editFaculty(Faculty faculty);

    Faculty deleteFaculty(long id);

    Collection<Faculty> getAllFaculty();
}
