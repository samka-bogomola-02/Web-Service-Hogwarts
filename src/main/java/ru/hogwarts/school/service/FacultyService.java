package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.interfaces.FacultyServiceInterface;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService implements FacultyServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //  CRUD-методы
    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Вызван метод для добавления факультета: {}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFacultyById(long id) {
        logger.info("Вызван метод для поиска факультета по ID: {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Факультет с id = {} не найден", id);
                    return new FacultyNotFoundException(id);
                });
    }

    @Override
    public Faculty editFaculty(Faculty facultyForUpdate) {
        logger.info("Вызван метод для редактирования факультета с id: {}", facultyForUpdate.getName());
        if (!facultyRepository.existsById(facultyForUpdate.getId())) {
            logger.error("Факультет с id = {} не найден для редактирования", facultyForUpdate.getId());
            throw new FacultyNotFoundException(facultyForUpdate.getId());
        }
        return facultyRepository.save(facultyForUpdate);
    }

    @Override
    public Faculty deleteFaculty(long id) {
        logger.info("Вызван метод для удаления факультета с id: {}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Факультет с id = {} не найден", id);
                    return new FacultyNotFoundException(id);
                        });
        facultyRepository.deleteById(faculty.getId());
        return faculty;
    }

    @Override
    public Collection<Faculty> getAllFaculty() {
        logger.info("Вызван метод для получения всех факультетов");
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getByColor(String color) {
        logger.info("Вызван метод для получения факультетов по цвету: {}", color);
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                        .toList();
    }

    public Faculty findByName(String name) {
        logger.info("Вызван метод для поиска факультета по имени: {}", name);
        return facultyRepository.findByNameIgnoreCase(name);
    }
    public Collection<Faculty> findByColor(String color) {
        logger.info("Вызван метод для получения факультетов по цвету: {}", color);
        return facultyRepository.findByColorContainsIgnoreCase(color);
    }
    public Collection<Faculty> findByNamePart(String part) {
        logger.info("Вызван метод для получения факультетов по части имени: {}", part);
        return facultyRepository.findByNameContainsIgnoreCase(part);
    }
}
