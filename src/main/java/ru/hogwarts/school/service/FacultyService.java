package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public Faculty deleteFaculty(long id) {
        Faculty faculty = findFaculty(id);
        if (faculty != null) {
            facultyRepository.deleteById(id);
        }
        return faculty;
    }

    public Collection<Faculty> findByColor(String color) {
        if (color == null || color.isBlank()) {
            return Collections.emptyList();
        }
        return facultyRepository.findByColor(color.toLowerCase());
    }

    public Collection<Faculty> findByNameOrColor(String nameOrColor) {
        if (nameOrColor == null || nameOrColor.isBlank()) {
            return Collections.emptyList();
        }
        String searchParam = nameOrColor.toLowerCase();
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchParam, searchParam);
    }

    public Collection<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty findFacultyWithStudents(long id) {
        return facultyRepository.findByIdWithStudents(id);
    }
}