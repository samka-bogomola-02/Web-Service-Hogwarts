package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchoolOfWitchcraftAndWizardryApplicationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private StudentController studentController;

                // MainController

    @Test
    void testGetHogwarts() throws Exception {
        Assertions.assertThat(this.template.getForObject("http://localhost:" + port + "/",
                String.class)).isNotNull();
    }

    @Test
    void defaultMessageHogwarts() throws Exception {
        Assertions.assertThat(this.template.getForObject("http://localhost:" + port + "/",
                String.class)).isEqualTo("Welcome to the School of Witchcraft and Wizardry!");
    }
}
