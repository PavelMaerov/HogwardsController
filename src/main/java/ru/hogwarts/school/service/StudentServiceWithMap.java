package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.stream.Collectors;

//@Service
public class StudentServiceWithMap {
    private long countId = 0;
    private final Map<Long, Student> students = new HashMap<>();

    public Student create(Student student) { //возвращаем нового студента c новым id
        Student newStudent = new Student(++countId, student.getName(), student.getAge());
        students.put(countId, newStudent);
        return newStudent;
    }
    public Student read(Long id) {
        Student student = students.get(id);
        if (student == null) throw new NotFoundException("студент", id);
        return student;
    }
    public Student update(Student student) {  //возвращаем старую версию студента
        Student oldStudent = students.replace(student.getId(), student);
        if (oldStudent == null) throw new NotFoundException("студент", student.getId());
        return oldStudent;
    }
    public Student delete(Long id) {  //возвращаем удаленного студента
        Student student = students.remove(id);
        if (student == null) throw new NotFoundException("студент", id);
        return student;
    }
    public List<Student> readByAge(int age) {
        return students.values().stream().filter(s->s.getAge()==age).collect(Collectors.toList());
    }
    public Collection<Student> readAll() {
        return students.values();
    }

}
