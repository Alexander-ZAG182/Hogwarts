package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.DTO.FacultyDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    void getFacultyById_shouldReturnFacultyJson() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Test Faculty");
        faculty.setColor("Red");

        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Faculty"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void createFaculty_shouldReturnSavedFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("New Faculty");
        faculty.setColor("Blue");

        when(facultyService.createFaculty(any(FacultyDTO.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Faculty\",\"color\":\"Blue\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Faculty"));
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Updated Faculty");
        faculty.setColor("Green");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Faculty\",\"color\":\"Green\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Faculty"));
    }

    @Test
    void getFacultiesByColor_shouldReturnFacultiesList() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Blue Faculty");
        faculty.setColor("Blue");

        when(facultyService.getFacultiesByColor("Blue")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty?color=Blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Blue Faculty"))
                .andExpect(jsonPath("$[0].color").value("Blue"));
    }

    @Test
    void getStudentsByFaculty_shouldReturnStudentsList() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Faculty with Students");

        when(facultyService.getFacultyWithStudents(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk());
    }
}