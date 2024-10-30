package ru.hogwarts.school.interfaces;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentServiceInterface {
    //  CRUD-методы
    Student addStudent(Student student);

    Student findStudentById(long id);

    Student editStudent(Student student);

    Student deleteStudent(long id);

    Collection<Student> getAllStudents();
}
