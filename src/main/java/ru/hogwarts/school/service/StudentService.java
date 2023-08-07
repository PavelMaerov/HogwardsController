package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student) { //возвращаем нового студента c новым id
        logger.info("Was invoked method 'create' for student - {} ", student);
        Student newStudent = new Student(0L, student.getName(), student.getAge(), null);
        return repository.save(newStudent);
    }
    public Student read(Long id) {
        logger.info("Was invoked method 'read' for student with id =  " + id);
        Optional<Student> student = repository.findById(id);
        if (student.isEmpty()){
            logger.error("There is not student with id = {}", id);
            throw new NotFoundException("студент", id);
        }

        return student.get();
    }
    public Student update(Student student) {  //возвращаем старую версию студента
        logger.info("Was invoked method 'update' for student - {}", student);
        Student oldStudent = read(student.getId()); //если такого нет - возникнет исключение
        //Интересный эффект: и read и save возвращают указатель на одно и то же место.
        //Это значит, что сохраняя результат read, мы не сохраняем ничего. save все перепишет
        oldStudent = new Student(oldStudent.getId(), oldStudent.getName(), oldStudent.getAge(), oldStudent.getFaculty());
        repository.save(student);
        return oldStudent;
    }
    public Student delete(Long id) {  //возвращаем удаленного студента
        logger.info("Was invoked method 'delete' for student with id = {}", id);
        Student oldStudent = read(id); //если такого нет - возникнет исключение
        repository.deleteById(id);
        return oldStudent;
    }
    public Set<Student> readByAge(int age) {
        logger.info("Was invoked method 'readByAge' with age = {}", age);
        return repository.findByAge(age);
    }
    public Set<Student> readByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method 'readByAgeBetween' with minAge = {}  and maxAge = {}", minAge, maxAge);
        return repository.findByAgeBetween(minAge, maxAge);
    }
    public Set<Student> readAll() {
        logger.info("Was invoked method 'readAll'");
        return new HashSet<>(repository.findAll());
    }
    public int studentCount() {
        logger.info("Was invoked method 'studentCount'");
        return repository.studentCount();
    }
    public double avgAge() {
        logger.info("Was invoked method 'avgAge'");
        return repository.avgAge();
    }
    public Set<Student> Last5() {
        logger.info("Was invoked method 'Last5'");
        return repository.Last5();
    }

}
