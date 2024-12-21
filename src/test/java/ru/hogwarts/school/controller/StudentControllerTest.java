package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void getStudentInfo() throws Exception { // id
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setAge(age);
        studentRepository.save(student);


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student.getId() + "/get",
                Student.class, student.getId()
        );

        System.out.println(response);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        Student body = response.getBody();
        assertThat(body.getName()).isEqualTo("Student1");
        assertThat(body.getAge()).isEqualTo(age);
    }
    @Test
    void getStudentInfoNotFound() throws Exception {
        long nonExistentId = 999L;
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + nonExistentId + "/get",
                Student.class, nonExistentId
        );
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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

    @Test
    void getByAge() {  //age
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setAge(age);
        studentRepository.save(student);


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
        System.out.println(response.getBody());
    }

    @Test
    void addStudentCorrect() throws Exception {
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setAge(age);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student, Student.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Student body = response.getBody();
        assertThat(body.getName()).isEqualTo(student.getName());
        assertThat(body.getAge()).isEqualTo(student.getAge());
        assertThat(body.getId()).isNotNull();
    }

    @Test
    void editStudent() {
        int age = nextInt(0, 100);
        Student student1 = new Student("Student1", age);
        studentRepository.save(student1);

        int age2 = nextInt(0, 100);
        Student updatedStudent = new Student("Student2", age2);
        updatedStudent.setId(student1.getId());

        ResponseEntity<Student> response = restTemplate.exchange(
                "/student/" + student1.getId() + "/edit",
                HttpMethod.PUT, new HttpEntity<>(updatedStudent),
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Student body = response.getBody();
        assertThat(body.getId()).isNotNull();
    }

    //удаление
    @Test
    void deleteStudent() { //  {id}/delete
        int age = nextInt(0, 100);
        Student student = new Student();
        student.setName("Student1");
        student.setAge(age);

        studentRepository.save(student);

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + student.getId() + "/delete",
                HttpMethod.DELETE, null, Student.class,
                student.getId()
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Student body = response.getBody();
        assertThat(body.getName()).isEqualTo(student.getName());
        assertThat(body.getAge()).isEqualTo(student.getAge());
        assertThat(body.getId()).isEqualTo(student.getId());

        Student actual = studentRepository.findById(body.getId()).orElse(null);
    }


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

    @Test
    void countStudents() {
        studentRepository.save(new Student("Alice", 20));
        studentRepository.save(new Student("Bob", 22));

        ResponseEntity<Long> response = restTemplate.getForEntity("http://localhost:" + port + "/student/count", Long.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(2L); // Проверяем, что количество студентов равно 2
    }

    @Test
    void averageAge() {
        studentRepository.save(new Student("Alice", 20));
        studentRepository.save(new Student("Bob", 30));

        ResponseEntity<Double> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/average-age", Double.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(25.0); // Проверяем, что средний возраст равен 25
    }

    @Test
    void lastFiveStudents() {
        for (int i = 1; i <= 7; i++) {
            studentRepository.save(new Student("Student" + i, 20 + i));
        }

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/last-five", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Student7");
    }

    @Test
    void getNamesStartingWithA() {
        studentRepository.save(new Student("Alice", 20));
        studentRepository.save(new Student("Bob", 22));
        studentRepository.save(new Student("Anna", 21));

        ResponseEntity<List<String>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/names-starting-with-a", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Приводим имена к нужному формату (первая буква заглавная, остальные строчные)
        List<String> expectedNames = Arrays.asList("Alice", "Anna");
        List<String> formattedResponseNames = response.getBody().stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase())
                .collect(Collectors.toList());

        assertThat(formattedResponseNames).containsExactlyInAnyOrderElementsOf(expectedNames);
    }

    @Test
    void printParallelStudents() {
        for (int i = 1; i <= 5; i++) {
            studentRepository.save(new Student("Student" + i, 20 + i));
        }

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/students/print-parallel", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5); // Проверяем, что возвращено 5 студентов
    }

    @Test
    void printSynchronizedStudents() {
        for (int i = 1; i <= 5; i++) {
            studentRepository.save(new Student("Student" + i, 20 + i));
        }

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/students/print-synchronized", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
    }
}
