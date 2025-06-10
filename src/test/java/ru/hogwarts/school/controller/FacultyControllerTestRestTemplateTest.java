package ru.hogwarts.school.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.DTO.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    void getStudentsByFaculty_shouldReturnStudentsList() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(
                getBaseUrl(), faculty, Faculty.class);
        Faculty savedFaculty = facultyResponse.getBody();

        StudentDTO student = new StudentDTO();
        student.setName("Harry Potter");
        student.setAge(15);

        Faculty facultyRef = new Faculty();
        facultyRef.setId(savedFaculty.getId());
        facultyRef.setName(faculty.getName());
        facultyRef.setColor(faculty.getColor());
        student.setFacultyId(facultyRef.getId());

        restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                StudentDTO.class
        );

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getBaseUrl() + "/" + savedFaculty.getId() + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertNotNull(response.getBody(), "Список студентов не должен быть null");
        assertFalse(response.getBody().isEmpty(), "Список студентов не должен быть пустым");
        assertEquals("Harry Potter", response.getBody().get(0).getName());
    }


    @Test
    void createFaculty_shouldReturnCreatedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("New Faculty");
        faculty.setColor("Blue");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                getBaseUrl(),
                faculty,
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("New Faculty", response.getBody().getName());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Old Name");
        faculty.setColor("Green");
        Faculty created = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        created.setName("Updated Name");
        HttpEntity<Faculty> request = new HttpEntity<>(created);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void deleteFaculty_shouldReturnOkStatus() {
        Faculty faculty = new Faculty();
        faculty.setName("To Delete");
        faculty.setColor("Yellow");
        Faculty created = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
}