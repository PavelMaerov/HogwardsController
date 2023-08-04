package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Set<Student> findByAge(int age);
    Set<Student> findByAgeBetween(int minAge, int maxAge);
    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    int studentCount();
    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
        //так тоже можно. используется только первое возвращаемое поле
        //@Query(value = "SELECT AVG(age) AS A, COUNT(*) AS C  FROM student", nativeQuery = true)
    double avgAge();
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Set<Student> Last5();

}
