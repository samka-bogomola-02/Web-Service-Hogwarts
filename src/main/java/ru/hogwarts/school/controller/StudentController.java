package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student student = studentService.findStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity findStudent(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) Integer age,
                                      @RequestParam(required = false) String namePart) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(studentService.findByName(name));
        }
        if (age != null && age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        if (namePart != null && !namePart.isBlank()) {
            return ResponseEntity.ok(studentService.findByNamePart(namePart));
        }
        return ResponseEntity.ok(studentService.getAllStudents());

    }

    @GetMapping("/age")
    public List<Student> getByAge(@RequestParam("age") int age) {
        List<Student> students = studentService.getByAge(age);
        System.out.println("students = " + students);
        return students;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<Student> editStudent(@PathVariable long id, Student student) {
        student.setId(id);
        Student student2 = studentService.editStudent(student);
        return ResponseEntity.ok(student2);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        try {
            Student student = studentService.deleteStudent(id);
            return ResponseEntity.ok().body(student); // Возвращаем статус 200 при успешном удалении
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если факультет не найден
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countStudents() {
        return ResponseEntity.ok(studentService.countAllStudents());
    }
    @GetMapping("/average-age") // возвращает с/а возраста
    public ResponseEntity<Double> AverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }
    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> lastFiveStudents() {
        return ResponseEntity.ok(studentService.findLastFiveStudents());
    }
}
