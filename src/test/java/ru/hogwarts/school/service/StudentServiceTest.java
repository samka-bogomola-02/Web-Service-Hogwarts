package ru.hogwarts.school.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;
    private Student student5;
    private Student student6;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student1 = new Student(1L, "John Doe", 20);
        student2 = new Student(2L, "Jane Doe", 22);
        student3 = new Student(3L, "Jim Beam", 21);
        student4 = new Student(4L, "Jack Daniels", 23);
        student5 = new Student(5L, "Johnny Walker", 24);
        student6 = new Student(6L, "Jameson Irish", 25);
        // Перенаправляем вывод в консоль для проверки
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Восстанавливаем оригинальный вывод
    }

    @Test
    void addStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student savedStudent = studentService.addStudent(student1);

        assertNotNull(savedStudent);
        assertEquals("John Doe", savedStudent.getName());
        verify(studentRepository, times(1)).save(student1);

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
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student updatedStudent = studentService.editStudent(student1);

        assertNotNull(updatedStudent);
        assertEquals("John Doe", updatedStudent.getName());
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    void deleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

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
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1));

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

    @Test
    void testFindByName() {
        when(studentRepository.findByNameIgnoreCase("John Doe")).thenReturn(student1);

        Student result = studentService.findByName("John Doe");

        assertEquals(student1, result);
        verify(studentRepository).findByNameIgnoreCase("John Doe");
    }

    @Test
    void testFindByAge() {
        when(studentRepository.findByAge(20)).thenReturn(Collections.singletonList(student1));

        Collection<Student> result = studentService.findByAge(20);

        assertEquals(1, result.size());
        assertTrue(result.contains(student1));
        verify(studentRepository).findByAge(20);
    }

    @Test
    void testFindByNamePart() {
        when(studentRepository.findByNameContainsIgnoreCase("John")).thenReturn(Collections.singletonList(student1));

        Collection<Student> result = studentService.findByNamePart("John");

        assertEquals(1, result.size());
        assertTrue(result.contains(student1));
        verify(studentRepository).findByNameContainsIgnoreCase("John");
    }

    @Test
    void testCountAllStudents() {
        when(studentRepository.countAllStudents()).thenReturn(5L);

        long count = studentService.countAllStudents();

        assertEquals(5L, count);
        verify(studentRepository).countAllStudents();
    }

    @Test
    void testGetAverageAge() {
        List<Student> students = Arrays.asList(new Student(1L, "John", 20), new Student(2L, "Jane", 30));
        when(studentRepository.findAll()).thenReturn(students);

        Double averageAge = studentService.getAverageAge();

        assertEquals(25.0, averageAge);
        verify(studentRepository).findAll();
    }

    @Test
    void testFindLastFiveStudents() {
        List<Student> lastFiveStudents = Arrays.asList(
                new Student(1L, "John", 20),
                new Student(2L, "Jane", 22),
                new Student(3L, "Doe", 21),
                new Student(4L, "Alice", 23),
                new Student(5L, "Bob", 24)
        );
        when(studentRepository.findLastFiveStudents()).thenReturn(lastFiveStudents);

        List<Student> result = studentService.findLastFiveStudents();

        assertEquals(lastFiveStudents, result);
        verify(studentRepository).findLastFiveStudents();
    }

    @Test
    void testGetNamesStartingWithA() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Alice", 20),
                new Student(2L, "Bob", 22),
                new Student(3L, "Anna", 21)
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<String> names = studentService.getNamesStartingWithA();

        assertEquals(Arrays.asList("ALICE", "ANNA"), names);
        verify(studentRepository).findAll();
    }

    @Test
    void testPrintParallelStudents() {
        List<Student> students = Arrays.asList(student1, student2, student3, student4, student5, student6);
        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn(6L); // Достаточное количество студентов

        List<Student> result = studentService.printParallelStudents();

        assertEquals(students, result);
        assertTrue(outputStreamCaptor.toString().contains("John Doe"));
        assertTrue(outputStreamCaptor.toString().contains("Jane Doe"));

        verify(studentRepository).findAll();
        verify(studentRepository).countAllStudents();
    }

    @Test
    void testPrintParallelStudentsInsufficientCount() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn(2L); // Недостаточное количество студентов

        List<Student> result = studentService.printParallelStudents();

        assertEquals(students, result);
        assertTrue(outputStreamCaptor.toString().contains("Недостаточно студентов для вывода."));

        verify(studentRepository).findAll();
        verify(studentRepository).countAllStudents();
    }

    @Test
    void testPrintSynchronizedStudents() {
        List<Student> students = Arrays.asList(student1, student2, student3, student4, student5, student6);
        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn(6L); // Достаточное количество студентов

        List<Student> result = studentService.printSynchronizedStudents();

        assertEquals(students, result);
        assertTrue(outputStreamCaptor.toString().contains("John Doe"));
        assertTrue(outputStreamCaptor.toString().contains("Jane Doe"));

        verify(studentRepository).findAll();
        verify(studentRepository).countAllStudents();
    }

    @Test
    void testPrintSynchronizedStudentsInsufficientCount() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn(2L); // Недостаточное количество студентов

        List<Student> result = studentService.printSynchronizedStudents();

        assertEquals(students, result);
        assertTrue(outputStreamCaptor.toString().contains("Недостаточно студентов для вывода."));

        verify(studentRepository).findAll();
        verify(studentRepository).countAllStudents();
    }

}