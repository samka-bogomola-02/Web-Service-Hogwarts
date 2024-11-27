package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.AssertionsForClassTypes.shouldHaveThrown;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StudentControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentController studentController;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private FacultyService facultyService;


    // получение

    @Test
    void getStudentInfo() throws Exception { // id
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setAge(age);
        student.setId(1L);
        studentRepository.save(student);

        System.out.println(student);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student.getId() + "/get",
                Student.class, student.getId()
        );

        System.out.println(response);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        Student body = response.getBody();
    }

    @Test
    void findStudent() { // all
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student",
                Student[].class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Student[] students = response.getBody();
        for (Student student : students) {
            System.out.println(student);
        }
    }
    //"http://localhost:8080/student/age?age=17"

        @Test
    void getByAge() {  //age
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setAge(age);
        student.setId(1L);
        studentRepository.save(student);

        System.out.println(student);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/age?age=" + student.getAge(),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {

                }
        );
        List<Student> students = response.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
    }

    //добавление

    @Test
    void addStudentCorrect() throws Exception {
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setId(1L);
        student.setAge(age);
        System.out.println("student: " + student);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student, Student.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Student body = response.getBody();
        assertThat(body.getName()).isEqualTo(student.getName());
        assertThat(body.getAge()).isEqualTo(student.getAge());
        assertThat(body.getId()).isEqualTo(student.getId());
    }


    //редактирование

    @Test
    void editStudent() {
//        int age = nextInt(0, 100);
//        Student student = new Student();
//        student.setName("Student1");
//        student.setId(1L);
//        student.setAge(age);
//
//        Student updatedStudent = new Student();
//        student.setName("Student2");
//        student.setAge(age);
    }

    //удаление

//    @Test //"http://localhost:8080/student/8/delete"
//    void deleteStudent() { //  {id}/delete
//        int age = nextInt(0, 100);
//        Student student = new Student();
//        student.setName("Student1");
//        student.setId(1L);
//        student.setAge(age);
//
//        studentRepository.save(student);
//
//        ResponseEntity<Student> response = restTemplate.exchange(
//                "http://localhost:" + port + "/student/" + student.getId()+ "/delete",
//                HttpMethod.DELETE, null, Student.class,
//                student.getId()
//                );
//
//        assertThat(response).isNotNull();
//        assertThat(response.getStatusCode()).isEqualTo(OK);
//        assertThat(response.getBody()).isNotNull();
//
//        Student body = response.getBody();
//        assertThat(body.getName()).isEqualTo(student.getName());
//        assertThat(body.getAge()).isEqualTo(student.getAge());
//        assertThat(body.getId()).isEqualTo(student.getId());
//
//        Student actual = studentRepository.findById(body.getId()).orElse(null);
//    }


    @Test
    public void testDeleteNonExistingStudent() {
        // Удаляем несуществующего студента
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/students/999/delete",
                HttpMethod.DELETE, null, Void.class,
                999);

        // Проверяем, что ответ имеет статус 404 Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}
