package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @SpyBean
    StudentService studentService;
    @MockBean
    StudentRepository studentRepository;
    @Autowired
    ObjectMapper objectMapper;

//    @Test
//    public void testGetStudentInfo() throws Exception {
//        Student student = new Student(1L, "Harry Potter", 11);
//        when(studentService.findStudentById(1L)).thenReturn(student);
//
//        mockMvc.perform(get("/student/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("Harry Potter"))
//                .andExpect(jsonPath("$.age").value(11));
//
//        verify(studentService).findStudentById(1L);
//    }
//
//
//    @Test
//    public void testAddStudent() throws Exception {
//        Student student = new Student(1L, "Hermione Granger", 12);
//        when(studentService.addStudent(any(Student.class))).thenReturn(new Student(
//                2L, "Hermione Granger", 12));
//
//        mockMvc.perform(post("/student")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(student)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("Hermione Granger"))
//                .andExpect(jsonPath("$.age").value(12));
//
//        verify(studentService).addStudent(any(Student.class));
//    }
//
//    @Test
//    public void testEditStudent() throws Exception {
//        Student updatedStudent = new Student(1L, "Ron Weasley", 12);
//        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);
//
//        mockMvc.perform(put("/student/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedStudent)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("Ron Weasley"))
//                .andExpect(jsonPath("$.age").value(12));
//
//        verify(studentService).editStudent(any(Student.class));
//    }

//    @Test
//    public void testDeleteStudent() throws Exception {
//        Student student = new Student(1L,"Student1", 13);
//        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
//
//        mockMvc.perform(get("/student/{id}").param("id", student.getId().toString()));
//    }
//    @Test
//    public void testFindStudentsByName() throws Exception {
//        Student student = new Student(1L, "Harry Potter", 11);
//        when(studentService.findByName("Harry Potter")).thenReturn((Student) Collections.singletonList(student));
//
//        mockMvc.perform(get("/student?name=Harry Potter"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
//
//        verify(studentService).findByName("Harry Potter");
//    }
//
//    @Test
//    public void testGetByAge() throws Exception {
//        Student student = new Student(1L, "Harry Potter", 11);
//        when(studentService.getByAge(11)).thenReturn(Collections.singletonList(student));
//
//        mockMvc.perform(get("/student/age?age=11"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
//
//        verify(studentService).getByAge(11);
//    }

}
