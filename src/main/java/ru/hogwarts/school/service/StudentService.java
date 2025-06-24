package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.DTO.StudentDTO;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student addStudent(StudentDTO student) {
        Faculty faculty = null;
        if (student.getFacultyId() != null && student.getFacultyId() != null) {
            faculty = facultyRepository.findById(student.getFacultyId())
                    .orElseThrow(() -> new IllegalArgumentException("Faculty not found with id: " + student.getFacultyId()));
        }
        Student studentForSave = new Student();
        studentForSave.setName(student.getName());
        studentForSave.setAge(student.getAge());
        studentForSave.setFaculty(faculty);
        return studentRepository.save(studentForSave);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            throw new IllegalArgumentException("Invalid age range parameters");
        }
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByAge(Integer age) {
        if (age == null) {
            return Collections.emptyList();
        }
        return studentRepository.findByAge(age);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Faculty getFacultyByStudentId(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent() && student.get().getFaculty() != null) {
            return student.get().getFaculty();
        } else throw new IllegalArgumentException("Student not found with id: " + id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Long getStudentsCount() {
        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        return studentRepository.findAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }
}
