package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findByColor(String color);

    Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    @Query("SELECT f FROM Faculty f LEFT JOIN FETCH f.students WHERE f.id = :id")
    Faculty findByIdWithStudents(@Param("id") Long id);
}