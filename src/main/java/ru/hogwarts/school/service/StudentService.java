package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<String> allNamesA() {
        //отсортированный список имен всех студентов в верхнем регистре, чье имя начинается с буквы А
        return repository.findAll().stream()
                .map(Student::getName)
                .filter(s->s.matches("[a,A].*")) //наверно startsWith быстрее, но для эксперимента сделал регулярное выражение
                .map(String::toUpperCase) //в верхний регистр перевожу после фильтрации, чтобы не переводить ненужные
                .sorted()
                .collect(Collectors.toList());
    }
    public double avgAgeByStream() {
        return repository.findAll().stream()
                .mapToInt(Student::getAge)
                .average().orElse(0);
    }
    public long sumMillion() {  //int переполнится от такой суммы и даже не выдаст ошибку
        long start = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        long finish = System.currentTimeMillis();
        logger.info("Execution time of 'sumMillion' method = {} мс",finish-start);
        return sum;
    }
    public long sumMillionParallel() {
        long start = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        long finish = System.currentTimeMillis();
        logger.info("Execution time of 'sumMillionParallel' method = {} мс",finish-start);
        return sum;
        //после распараллеливания получил ухудшение примерно в 2 раза
    }
}
