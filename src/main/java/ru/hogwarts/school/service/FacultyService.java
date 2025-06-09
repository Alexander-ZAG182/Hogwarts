package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.DTO.FacultyDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudentsByFacultyId(long facultyId) {
        return studentRepository.findStudentsByFacultyId(facultyId);
    }

    public Faculty getFacultyWithStudents(long id) {
        return facultyRepository.findByIdWithStudents(id);
    }

    public Faculty createFaculty(FacultyDTO faculty) {
        Faculty facultyEntity = new Faculty();
        facultyEntity.setName(faculty.getName());
        facultyEntity.setColor(faculty.getColor());
        return facultyRepository.save(facultyEntity);
    }

    public Faculty getFacultyById(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        if (color == null || color.isBlank()) {
            return Collections.emptyList();
        }
        return facultyRepository.findByColor(color.toLowerCase());
    }

    public Collection<Faculty> findFacultiesByNameOrColor(String nameOrColor) {
        if (nameOrColor == null || nameOrColor.isBlank()) {
            return Collections.emptyList();
        }
        String searchParam = nameOrColor.toLowerCase();
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchParam, searchParam);
    }
}