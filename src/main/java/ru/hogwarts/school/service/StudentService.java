package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.interfaces.StudentServiceInterface;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService implements StudentServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //  CRUD-методы
    @Override
    public Student addStudent(Student student) {
        logger.info("Вызван метод для добавления студента");
        return studentRepository.save(student);
    }

    @Override
    public Student findStudentById(long id) {
        logger.info("Вызван метод для поиска студента по id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Студент с id = {} не найден", id);
                    return new StudentNotFoundException(id);
                });
    }

    @Override
    public Student editStudent(Student studentForUpdate) {
        logger.info("Вызван метод для редактирования студента с id: {}", studentForUpdate.getId());
        if (!studentRepository.existsById(studentForUpdate.getId())) {
            logger.error("Студент с id = {} не найден для редактирования", studentForUpdate.getId());
            throw new StudentNotFoundException(studentForUpdate.getId());
        }
        return studentRepository.save(studentForUpdate);
    }

    @Override
    public Student deleteStudent(long id) {
        logger.info("Вызван метод для удаления студента с id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Студент с id = {} не найден", id);
                    return new StudentNotFoundException(id);
                });
        studentRepository.deleteById(student.getId());
        return student;
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Вызван метод для получения всех студентов");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getByAge(int age) {
        logger.info("Вызван метод для получения студентов по возрасту: {}", age);
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age).toList();
    }

    public Student findByName(String name) {
        logger.info("Вызван метод для поиска студента по имени: {}", name);
        return studentRepository.findByNameIgnoreCase(name);
    }

    public Collection<Student> findByAge(Integer age) {
        logger.debug("Вызван метод для поиска студентов по возрасту: {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByNamePart(String part) {
        logger.debug("Вызван метод для поиска студентов по части имени: {}", part);
        return studentRepository.findByNameContainsIgnoreCase(part);
    }

    @Override
    public long countAllStudents() {
        logger.info("Вызван метод для получения общего количества студентов");
        return studentRepository.countAllStudents();
    }

    @Override
    public Double getAverageAge() {
        logger.info("Вызван метод для получения среднего возраста студентов");
            List<Student> students = studentRepository.findAll();
            return students.stream()
                    .mapToInt(Student::getAge)
                    .average()
                    .orElse(0.0); // Если список пуст, возвращаем 0.0
    }

    @Override
    public List<Student> findLastFiveStudents() {
        logger.info("Вызван метод для поиска последних пяти студентов");
        return studentRepository.findLastFiveStudents();
    }

    @Override
    public List<String> getNamesStartingWithA() {
        logger.info("Вызван метод для получения имен студентов, начинающихся с буквы 'A'");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }
}
