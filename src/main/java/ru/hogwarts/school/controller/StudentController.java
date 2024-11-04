package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student student = studentService.findStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/age")
    public ResponseEntity<Collection<Student>> getByAge(@RequestParam("age") int age) {
        if (age < 0 || age > 120) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.getByAge(age));
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> editStudent(@PathVariable long id, Student student) {
        student.setId(id);
        Student student2 = studentService.editStudent(student);
        return ResponseEntity.ok(student2);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content при успешном удалении
        } catch (FacultyNotFoundException e) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если факультет не найден
        }
    }
}
