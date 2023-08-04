package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student) { //возвращаем нового студента c новым id
        Student newStudent = new Student(0L, student.getName(), student.getAge(), null);
        return repository.save(newStudent);
    }
    public Student read(Long id) {
        Optional<Student> student = repository.findById(id);
        if (student.isEmpty()) throw new NotFoundException("студент", id);
        return student.get();
    }
    public Student update(Student student) {  //возвращаем старую версию студента
        Student oldStudent = read(student.getId()); //если такого нет - возникнет исключение
        //Интересный эффект: и read и save возвращают указатель на одно и то же место.
        //Это значит, что сохраняя результат read, мы не сохраняем ничего. save все перепишет
        oldStudent = new Student(oldStudent.getId(), oldStudent.getName(), oldStudent.getAge(), oldStudent.getFaculty());
        repository.save(student);
        return oldStudent;
    }
    public Student delete(Long id) {  //возвращаем удаленного студента
        Student oldStudent = read(id); //если такого нет - возникнет исключение
        repository.deleteById(id);
        return oldStudent;
    }
    public Set<Student> readByAge(int age) {
        return repository.findByAge(age);
    }
    public Set<Student> readByAgeBetween(int minAge, int maxAge) {
        return repository.findByAgeBetween(minAge, maxAge);
    }
    public Set<Student> readAll() {
        return new HashSet<>(repository.findAll());
    }
    public int studentCount() {
        return repository.studentCount();
    }
    public double avgAge() {
        return repository.avgAge();
    }
    public Set<Student> Last5() {
        return repository.Last5();
    }

}
