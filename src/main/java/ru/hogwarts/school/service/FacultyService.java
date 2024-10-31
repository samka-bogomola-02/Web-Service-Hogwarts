package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.interfaces.FacultyServiceInterface;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class FacultyService implements FacultyServiceInterface {
    private final HashMap<Long, Faculty> storageList = new HashMap<>();
    private long lastId = 0;

    //  CRUD-методы
    @Override
    public Faculty addFaculty(Faculty faculty){
        faculty.setId(++lastId);
        storageList.put(lastId, faculty);
        return faculty;
    }
    @Override
    public Faculty findFacultyById(long id) {
        return storageList.get(id);
    }
    @Override
    public Faculty editFaculty(Faculty faculty) {
        if (storageList.containsKey(faculty.getId())) {
            storageList.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }
    @Override
    public Faculty deleteFaculty(long id) {
        storageList.remove(id);
        return storageList.get(id);
    }
    @Override
    public Collection<Faculty> getAllFaculty() {
        return storageList.values();
    }

    @Override
    public List<Faculty> getByColor(String color) {
        return storageList.values().stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .toList();//get filtered faculty
    }
}
