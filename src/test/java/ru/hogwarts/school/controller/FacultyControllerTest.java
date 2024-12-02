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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    final String name = "Name1";
    final String color = "Red";
    final long id = 1L;
    final String name2 = "Name2";
    final String color2 = "blue";
    final long id2 = 2L;
    final String name3 = "Name3";
    final String color3 = "orange";
    final long id3 = 3L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void saveFacultyTest() throws Exception {
        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", name);
        facultyJson.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void findFacultyTest() throws Exception {

        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", name);
        facultyJson.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void editeFacultyTest() throws Exception {

        final long idFacultyCol = 1L;

        Faculty facultyCol = new Faculty();
        facultyCol.setId(idFacultyCol);
        facultyCol.setName(name2);
        facultyCol.setColor(color2);

        JSONObject facultyJsonCol = new JSONObject();
        facultyJsonCol.put("name", name2);
        facultyJsonCol.put("color", color2);

        Faculty faculty = new Faculty();
        faculty.setId(idFacultyCol);
        faculty.setName(name);
        faculty.setColor(color);

        JSONObject facultyJson = new JSONObject();
        facultyJson.put("id", idFacultyCol);
        facultyJson.put("name", name);
        facultyJson.put("color", color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(facultyCol));
        when(facultyRepository.existsById(idFacultyCol)).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + idFacultyCol)
                        .content(facultyJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idFacultyCol))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.existsById(id)).thenReturn(true);
        doNothing().when(facultyRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getFacultyByColorTest() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(id);
        faculty1.setName(name);
        faculty1.setColor("gray");

        Faculty student2 = new Faculty();
        student2.setId(id2);
        student2.setName(name2);
        student2.setColor("gray");

        List<Faculty> faculties = Arrays.asList(faculty1, student2);

        // Настройка мока
        when(studentService.findByColor("gray")).thenReturn(faculties);

        // Выполнение запроса
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color")
                        .param("color", "gray")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}