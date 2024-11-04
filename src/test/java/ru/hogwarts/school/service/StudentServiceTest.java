package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student(1L, "John Doe", 20);
        // Предположим, что у вас есть конструктор с параметрами
    }

    @Test
    void addStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student savedStudent = studentService.addStudent(student);

        assertNotNull(savedStudent);
        assertEquals("John Doe", savedStudent.getName());
        verify(studentRepository, times(1)).save(student);

    }

    @Test
    void findStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.findStudentById(1L));
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void editStudentExceptions() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student updatedStudent = studentService.editStudent(student);

        assertNotNull(updatedStudent);
        assertEquals("John Doe", updatedStudent.getName());
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void deleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student deletedStudent = studentService.deleteStudent(1L);

        assertNotNull(deletedStudent);
        assertEquals("John Doe", deletedStudent.getName());
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStudent_shouldThrowException_whenNotExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        Collection<Student> students = studentService.getAllStudents();

        assertNotNull(students);
        assertEquals(1, students.size());
    }

    @Test
    void getByAge() {

        when(studentRepository.findAll()).thenReturn(Arrays.asList(
                new Student(1L, "John Doe", 20),
                new Student(2L, "Jane Doe", 21)
        ));

        List<Student> students = studentService.getByAge(20);

        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).getName());
        verify(studentRepository, times(1)).findAll();
    }
}