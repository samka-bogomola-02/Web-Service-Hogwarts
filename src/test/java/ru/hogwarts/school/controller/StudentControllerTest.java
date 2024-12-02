package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    final String name = "Name1";
    final int age = 15;
    final long id = 1L;
    final String name2 = "Name2";
    final int age2 = 16;
    final long id2 = 2L;
    final String name3 = "Name3";
    final int age3 = 15;
    final long id3 = 3L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void saveStudentTest() throws Exception {

        JSONObject studentJson = new JSONObject();
        studentJson.put("name", name);
        studentJson.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void findStudentTest() throws Exception {

        JSONObject studentJson = new JSONObject();
        studentJson.put("name", name);
        studentJson.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void editeStudentTest() throws Exception {

        final long idStudentOld = 1L;

        Student studentOld = new Student();
        studentOld.setId(idStudentOld);
        studentOld.setName(name2);
        studentOld.setAge(age2);

        JSONObject studentJsonOld = new JSONObject();
        studentJsonOld.put("name", name2);
        studentJsonOld.put("age", age2);

        Student student = new Student();
        student.setId(idStudentOld);
        student.setName(name);
        student.setAge(age);

        JSONObject studentJson = new JSONObject();
        studentJson.put("id", idStudentOld);
        studentJson.put("name", name);
        studentJson.put("age", age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(studentOld));
        when(studentRepository.existsById(idStudentOld)).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + idStudentOld)
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idStudentOld))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(studentRepository.existsById(id)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentsByAgeTest() throws Exception {
        Student student1 = new Student();
        student1.setId(id);
        student1.setName(name);
        student1.setAge(15);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(15); // Также совпадает

        List<Student> students = Arrays.asList(student1, student2);

        // Настройка мока
        when(studentService.findByAge(15)).thenReturn(students);

        // Выполнение запроса
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age")
                        .param("age", "15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}


