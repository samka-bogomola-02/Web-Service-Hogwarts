package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.interfaces.StudentServiceInterface;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentService implements StudentServiceInterface {
    private final HashMap<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    //  CRUD-методы
    @Override
    public Student addStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    @Override
    public Student findStudentById(long id) {
        return students.get(id);
    }
    @Override
    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
        students.put(student.getId(), student);
        return student;
        }
        return null;
    }
    @Override
    public Student deleteStudent(long id) {
        students.remove(id);
        return students.get(id);
    }
    @Override
    public Collection<Student> getAllStudents() {
        return students.values();
    }

    @Override
    public List<Student> getByAge(int age) {//age
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .toList();//get filtered students
    }

}
