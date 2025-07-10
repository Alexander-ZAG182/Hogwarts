package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.DTO.FacultyDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudentsByFacultyId(long facultyId) {
        logger.info("Was invoked method: getStudentsByFacultyId(facultyId={})", facultyId);
        return studentRepository.findStudentsByFacultyId(facultyId);
    }

    public Faculty getFacultyWithStudents(long id) {
        logger.info("Was invoked method: getFacultyWithStudents(id={})", id);
        return facultyRepository.findByIdWithStudents(id);
    }

    public Faculty createFaculty(FacultyDTO faculty) {
        logger.info("Was invoked method: createFaculty");
        Faculty facultyEntity = new Faculty();
        facultyEntity.setName(faculty.getName());
        facultyEntity.setColor(faculty.getColor());
        return facultyRepository.save(facultyEntity);
    }

    public Faculty getFacultyById(long id) {
        logger.debug("Was invoked method: getFacultyById(id={})", id);
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method: updateFaculty(id={})", faculty.getId());
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        logger.warn("Faculty not found for update: id={}", faculty.getId());
        return null;
    }

    public void deleteFaculty(long id) {
        logger.warn("Was invoked method: deleteFaculty(id={})", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method: getAllFaculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.debug("Was invoked method: getFacultiesByColor(color={})", color);
        if (color == null || color.isBlank()) {
            logger.warn("Invalid color parameter: null or blank");
            return Collections.emptyList();
        }
        return facultyRepository.findByColor(color.toLowerCase());
    }

    public Collection<Faculty> findFacultiesByNameOrColor(String nameOrColor) {
        logger.debug("Was invoked method: findFacultiesByNameOrColor(nameOrColor={})", nameOrColor);
        if (nameOrColor == null || nameOrColor.isBlank()) {
            logger.warn("Invalid search parameter: null or blank");
            return Collections.emptyList();
        }
        String searchParam = nameOrColor.toLowerCase();
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchParam, searchParam);
    }

    public String getLongestFacultyName() {
        logger.info("Was invoked method: getLongestFacultyName");
        return facultyRepository.findAllFacultyNames().stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }
}
