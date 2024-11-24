package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.StudentRepository;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.AssertionsForClassTypes.shouldHaveThrown;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    private final Faker faker = new Faker();

    // получение

    @Test
    void getStudentInfo() throws Exception { // id
//        Student student = new Student(faker.harryPotter().character(),nextInt());
//        studentRepository.save(student);
//
//        ResponseEntity<Student> entity = restTemplate.getForEntity("http://localhost:" + port + "/student/{id}",
//                Student.class, student.getId());
//
//        AssertionsForClassTypes.assertThat(entity).isNotNull();
//        AssertionsForClassTypes.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        AssertionsForClassTypes.assertThat(entity.getBody()).isNotNull();
//        Student body = entity.getBody();
//        assertThat(body.getName()).isEqualTo(student.getName());
//        assertThat(body.getAge()).isEqualTo(student.getAge());
//        System.out.println("Response body: " + entity.getBody());
    }

    @Test
    void findStudent() { // all
    }

    @Test
    void getByAge() {  //age                                //todo: add test
    }

    //добавление

    @Test
    void addStudentCorrect() throws Exception{
        Student student = new Student(faker.harryPotter().character(),nextInt());

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student", student,
                Student.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Student body = response.getBody();
        assertThat(body.getName()).isEqualTo(student.getName());
        assertThat(body.getAge()).isEqualTo(student.getAge());
        assertThat(body.getId()).isEqualTo(student.getId());
        System.out.println("Response body: " + response.getBody());

    }

    //редактирование

    @Test
    void editStudent() {                                 //todo: add test
    }

    //удаление

    @Test
    void deleteStudent() {                               //todo: add test
    }
}
