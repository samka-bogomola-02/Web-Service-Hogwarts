package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.interfaces.FacultyServiceInterface;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService implements FacultyServiceInterface {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //  CRUD-методы
    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFacultyById(long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    @Override
    public Faculty editFaculty(Faculty facultyForUpdate) {
        if (!facultyRepository.existsById(facultyForUpdate.getId())) {
            throw new FacultyNotFoundException(facultyForUpdate.getId());
        }
        return facultyRepository.save(facultyForUpdate);
    }

    @Override
    public Faculty deleteFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.deleteById(faculty.getId());
        return faculty;
    }

    @Override
    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                        .toList();
    }

    public Faculty findByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }
    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColorContainsIgnoreCase(color);
    }
    public Collection<Faculty> findByNamePart(String part) {
        return facultyRepository.findByNameContainsIgnoreCase(part);
    }
}
