package ru.hogwarts.school.repository;

import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
