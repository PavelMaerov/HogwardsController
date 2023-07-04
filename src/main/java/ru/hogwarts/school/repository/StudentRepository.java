package ru.hogwarts.school.repository;

import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Set<Student> findByAge(int age);
    Set<Student> findByAgeBetween(int minAge, int maxAge);
}
