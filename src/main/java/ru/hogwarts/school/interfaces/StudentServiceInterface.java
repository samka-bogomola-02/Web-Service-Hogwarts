package ru.hogwarts.school.interfaces;

import org.springframework.data.domain.Page;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentInterface;

import java.util.Collection;
import java.util.List;

public interface StudentServiceInterface {
    //  CRUD-методы
    Student addStudent(Student student);

    Student findStudentById(long id);

    Student editStudent(Student studentForUpdate);

    Student deleteStudent(long id);

    Collection<Student> getAllStudents();
    List<Student> getByAge(int age);
    long countAllStudents();

    Double getAverageAge();

    List<Student> findLastFiveStudents();
}
