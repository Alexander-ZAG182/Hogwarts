package ru.hogwarts.school.controller;

import jakarta.transaction.Transactional;
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
import ru.hogwarts.school.model.DTO.FacultyDTO;
import ru.hogwarts.school.model.DTO.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void getStudentInfo_shouldReturnStudentWhenExists() {
        StudentDTO student = new StudentDTO();
        student.setName("Harry Potter");
        student.setAge(15);

        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class);
        Student createdStudent = createResponse.getBody();

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdStudent.getId(),
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Harry Potter", response.getBody().getName());
        restTemplate.delete(getBaseUrl() + "/" + createdStudent.getId());
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() {
        StudentDTO student = new StudentDTO();
        student.setName("Hermione Granger");
        student.setAge(15);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Hermione Granger", response.getBody().getName());
        restTemplate.delete(getBaseUrl() + "/" + response.getBody().getId());
    }

    @Test
    void editStudent_shouldUpdateStudent() {
        StudentDTO student = new StudentDTO();
        student.setName("Ron Weasley");
        student.setAge(14);

        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class);

        Student createdStudent = createResponse.getBody();

        createdStudent.setAge(15);
        HttpEntity<Student> request = new HttpEntity<>(createdStudent);
        ResponseEntity<Student> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(15, response.getBody().getAge());
        restTemplate.delete(getBaseUrl() + "/" + createdStudent.getId());
    }

    @Test
    void deleteStudent_shouldRemoveStudent() {
        StudentDTO student = new StudentDTO();
        student.setName("Neville Longbottom");
        student.setAge(14);

        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class);
        Student createdStudent = createResponse.getBody();

        restTemplate.delete(getBaseUrl() + "/" + createdStudent.getId());

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdStudent.getId(),
                Student.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getStudentsByAge_shouldReturnFilteredStudents() {
        restTemplate.postForEntity(getBaseUrl(),
                new Student(null, "Draco Malfoy", 15), Student.class);
        restTemplate.postForEntity(getBaseUrl(),
                new Student(null, "Luna Lovegood", 14), Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getBaseUrl() + "?age=15",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Draco Malfoy", response.getBody().get(0).getName());
    }

    @Test
    void getStudentsByAgeRange_shouldReturnStudentsInRange() {
        restTemplate.postForEntity(getBaseUrl(),
                new Student(null, "Ginny Weasley", 13), Student.class);
        restTemplate.postForEntity(getBaseUrl(),
                new Student(null, "Fred Weasley", 17), Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getBaseUrl() + "/age-between?minAge=14&maxAge=16",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getFacultyByStudent_shouldReturnStudentFaculty() {
        FacultyDTO faculty = new FacultyDTO();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class);

        StudentDTO student = new StudentDTO();
        student.setName("Harry Potter");
        student.setAge(15);
        student.setFacultyId(createdFaculty.getId());
        Student createdStudent = restTemplate.postForObject(
                getBaseUrl(),
                student,
                Student.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdStudent.getId() + "/faculty",
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Gryffindor", response.getBody().getName());
        restTemplate.delete(getBaseUrl() + "/" + createdStudent.getId());
    }
}
