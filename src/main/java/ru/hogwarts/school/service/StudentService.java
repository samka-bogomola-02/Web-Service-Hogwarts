package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.interfaces.StudentServiceInterface;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService implements StudentServiceInterface {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //  CRUD-методы
    @Override
    public Student addStudent(Student student) {
        System.out.println("studentRepository.save(student) = " + studentRepository.save(student));
        return studentRepository.save(student);
    }

    @Override
    public Student findStudentById(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student editStudent(Student studentForUpdate) {
        if (!studentRepository.existsById(studentForUpdate.getId())) {
            throw new StudentNotFoundException(studentForUpdate.getId());
        }
        return studentRepository.save(studentForUpdate);
    }

    @Override
    public Student deleteStudent(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.deleteById(student.getId());
        return student;
    }

    @Override
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age).toList();
    }
    public Student findByName(String name) {
        return studentRepository.findByNameIgnoreCase(name);
    }
    public Collection<Student> findByAge(Integer age) {
        return studentRepository.findByAge(age);
    }
    public Collection<Student> findByNamePart(String part) {
        return studentRepository.findByNameContainsIgnoreCase(part);
    }

}
