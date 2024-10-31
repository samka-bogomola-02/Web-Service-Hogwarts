package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    @Test
    void addStudent() {
        StudentService studentService = new StudentService();

        Student student1 = new Student(null, "John Doe", 18); // Добавляем первого студента
        Student addedStudent1 = studentService.addStudent(student1);

        assertNotNull(addedStudent1);// Проверяем, что студент был добавлен
        assertEquals("John Doe", addedStudent1.getName());
        assertEquals(18, addedStudent1.getAge());
        assertEquals(1L, addedStudent1.getId()); // Проверяем, что ID равен 1

        Student student2 = new Student(null, "Jane Smith", 19); // Добавляем второго студента
        Student addedStudent2 = studentService.addStudent(student2);

        assertNotNull(addedStudent2); // Проверяем, что второй студент был добавлен
        assertEquals("Jane Smith", addedStudent2.getName());
        assertEquals(19, addedStudent2.getAge());
        assertEquals(2L, addedStudent2.getId()); // Проверяем, что ID равен 2

    }

    @Test
    void findStudentById() {
        StudentService studentService = new StudentService();
        Student student = new Student(1L, "I love java ", 18);
        student.setName("but I don't like tests");
        student.setAge(22);
        studentService.addStudent(student);

        Student foundStudent = studentService.findStudentById(1);

        assertNotNull(foundStudent);
        assertEquals("but I don't like tests", foundStudent.getName());
    }

    @Test
    void editStudent() {
        StudentService studentService = new StudentService();
        Student student = new Student(1L, "I love java ", 18);
        student.setName("but I don't like tests");
        student.setAge(22);
        studentService.addStudent(student);

        student.setName("I love tests");
        student.setAge(25);
        Student editedStudent = studentService.editStudent(student);

        assertNotNull(editedStudent);
        assertEquals("I love tests", editedStudent.getName());
        assertEquals(25, editedStudent.getAge());
        // Проверка на несуществующего студента
        Student nonExistentStudent = new Student(2L, "I don't exist", 20);
        assertNull(studentService.editStudent(nonExistentStudent)); // Проверяем, что результат равен null
    }

    @Test
    void deleteStudent() {
        StudentService studentService = new StudentService();
        Student student = new Student(1L, "I love java ", 18);
        student.setName("but I don't like tests");
        student.setAge(22);
        studentService.addStudent(student);
        studentService.deleteStudent(1);
        Student deletedStudent = studentService.findStudentById(1);
        assertNull(deletedStudent);
    }

    @Test
    void getAllStudents() {
        StudentService studentService = new StudentService();
        Student student1 = new Student(1L, "John Doe", 18);
        Student student2 = new Student(2L, "Jane Smith", 19);
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        Collection<Student> allStudents = studentService.getAllStudents();

        assertNotNull(allStudents);
        assertEquals(2, allStudents.size());
        assertTrue(allStudents.contains(student1));
        assertTrue(allStudents.contains(student2));
    }

    @Test
    void getByAge() {
        StudentService studentService = new StudentService();
        Student student1 = new Student(1L, "John Doe", 18);
        Student student2 = new Student(2L, "Jane Smith", 19);
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        Collection<Student> studentsByAge = studentService.getByAge(18);

        assertNotNull(studentsByAge);
        assertEquals(1, studentsByAge.size());
        assertTrue(studentsByAge.contains(student1));
        Collection<Student> studentsByAge2 = studentService.getByAge(19);

        assertNotNull(studentsByAge2);
        assertEquals(1, studentsByAge2.size());
        assertTrue(studentsByAge2.contains(student2));
        Collection<Student> studentsByAge3 = studentService.getByAge(20);

        assertNotNull(studentsByAge3);
        assertEquals(0, studentsByAge3.size());
    }
}