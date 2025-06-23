package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query("SELECT s FROM Student s WHERE s.faculty.id = :facultyId")
    List<Student> findStudentsByFacultyId(@Param("facultyId") Long facultyId);

    @Query("SELECT COUNT(s) FROM Student s")
    Long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double findAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    List<Student> findLastFiveStudents();
}



