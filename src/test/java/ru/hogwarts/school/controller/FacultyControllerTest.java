package ru.hogwarts.school.controller;

import org.assertj.core.api.AssertionsForInterfaceTypes;
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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyService facultyService;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
    }

    @Test
    void getFacultyInfo() throws Exception { // id
        Faculty faculty = new Faculty();
        faculty.setName("Faculty1");
        faculty.setColor("green");
        facultyRepository.save(faculty);

        System.out.println(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                Faculty.class, faculty.getId()
        );

        System.out.println(response);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        Faculty body = response.getBody();
    }

    @Test
    void findFaculty() { // all
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty",
                Faculty[].class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Faculty[] faculties = response.getBody();
        for (Faculty faculty : faculties) {
            System.out.println(faculty);
        }
    }

    @Test
    void getByColor() {  //color
        Faculty faculty = new Faculty();
        faculty.setName("Faculty1");
        faculty.setColor("green");
        facultyRepository.save(faculty);


        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/color?color=" + faculty.getColor(),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {

                }
        );
        List<Faculty> faculties = response.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        AssertionsForInterfaceTypes.assertThat(response.getBody()).isNotNull();
        System.out.println(response.getBody());
    }

    @Test
    void addFacultyCorrect() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty1");
        faculty.setColor("green");
        facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty, Faculty.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Faculty body = response.getBody();
        assertThat(body.getName()).isEqualTo(faculty.getName());
        assertThat(body.getColor()).isEqualTo(faculty.getColor());
        assertThat(body.getId()).isNotNull();
    }

    @Test
    void editFaculty() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Faculty1");
        faculty1.setColor("green");
        facultyRepository.save(faculty1);

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(faculty1.getId());
        updatedFaculty.setName("Faculty2");
        updatedFaculty.setColor("blue");

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty/" + faculty1.getId(), HttpMethod.PUT,
                new HttpEntity<>(updatedFaculty), Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Faculty body = response.getBody();
        assertThat(body.getId()).isNotNull();
    }

    @Test
    void deleteFaculty() { //  {id}
        Faculty faculty = new Faculty();
        faculty.setName("Faculty1");
        faculty.setColor("green");

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                HttpMethod.DELETE, null, Faculty.class,
                faculty.getId()
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();

        Faculty body = response.getBody();
        assertThat(body.getName()).isEqualTo(faculty.getName());
        assertThat(body.getColor()).isEqualTo(faculty.getColor());
        assertThat(body.getId()).isEqualTo(faculty.getId());

        Faculty actual = facultyRepository.findById(body.getId()).orElse(null);
    }

    @Test
    public void testDeleteNonExistingFaculty() {
        // Удаляем несуществующий факультет
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/999",
                HttpMethod.DELETE, null, Void.class,
                999);

        // Проверяем, что ответ имеет статус 404 Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void testGetLongestFacultyName() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Faculty");
        faculty1.setColor("green");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Facultyy");
        faculty2.setColor("green");
        facultyRepository.save(faculty2);

        Faculty faculty3 = new Faculty();
        faculty3.setName("Facultyyy");
        faculty3.setColor("green");
        facultyRepository.save(faculty3);

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/longest-name", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Facultyyy");
    }
}
