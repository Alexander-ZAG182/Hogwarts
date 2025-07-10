package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.DTO.StudentDTO;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(StudentDTO student) {
        logger.info("Was invoked method: addStudent");
        Faculty faculty = null;
        if (student.getFacultyId() != null) {
            faculty = facultyRepository.findById(student.getFacultyId())
                    .orElseThrow(() -> {
                        logger.error("Faculty not found: id={}", student.getFacultyId());
                        return new IllegalArgumentException("Faculty not found with id: " + student.getFacultyId());
                    });
        }
        Avatar avatar = null;
        if (student.getAvatarId() != null) {
            avatar = avatarRepository.getReferenceById(student.getAvatarId());
        }

        Student studentForSave = new Student();
        studentForSave.setName(student.getName());
        studentForSave.setAge(student.getAge());
        studentForSave.setFaculty(faculty);
        studentForSave.setAvatar(avatar);
        return studentRepository.save(studentForSave);
    }

    public Student getStudentById(Long id) {
        logger.debug("Was invoked method: getStudentById(id={})", id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method: updateStudent(id={})", student.getId());
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        logger.warn("Student not found for update: id={}", student.getId());
        return null;
    }

    public void deleteStudent(Long id) {
        logger.warn("Was invoked method: deleteStudent(id={})", id);
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        logger.debug("Was invoked method: findByAge(age={})", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.debug("Was invoked method: findByAgeBetween(min={}, max={})", minAge, maxAge);
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            logger.error("Invalid age range: min={}, max={}", minAge, maxAge);
            throw new IllegalArgumentException("Invalid age range parameters");
        }
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByAge(Integer age) {
        logger.debug("Was invoked method: getStudentsByAge(age={})", age);
        if (age == null) {
            logger.warn("Age parameter is null");
            return Collections.emptyList();
        }
        return studentRepository.findByAge(age);
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method: getAllStudents");
        return studentRepository.findAll();
    }

    public Faculty getFacultyByStudentId(Long id) {
        logger.debug("Was invoked method: getFacultyByStudentId(id={})", id);
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent() && student.get().getFaculty() != null) {
            return student.get().getFaculty();
        } else {
            logger.error("Student not found or has no faculty: id={}", id);
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
    }

    public List<Student> findAll() {
        logger.info("Was invoked method: findAll");
        return studentRepository.findAll();
    }

    public Long getStudentsCount() {
        logger.info("Was invoked method: getStudentsCount");
        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method: getAverageAge");
        return studentRepository.findAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method: getLastFiveStudents");
        return studentRepository.findLastFiveStudents();
    }

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method: getStudentNamesStartingWithA");
        return studentRepository.findAllStudentNames().stream()
                .filter(name -> name != null &&
                        (name.toUpperCase().startsWith("А") ||
                                name.toUpperCase().startsWith("A")))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeViaStream() {
        logger.info("Was invoked method: getAverageAgeViaStream");
        return studentRepository.findAllStudentAges().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
