package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;

import java.util.Set;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Set<Faculty> findByColor(String color);
    Set<Faculty> findByNameIgnoreCase(String name);
    Set<Faculty> findByColorIgnoreCase(String color);
    Set<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}
