package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student) { //возвращаем нового студента c новым id
        Student newStudent = new Student(0L, student.getName(), student.getAge());
        return repository.save(newStudent);
    }
    public Student read(Long id) {
        Optional<Student> student = repository.findById(id);
        if (student.isEmpty()) throw new NotFoundException("студент", id);
        return student.get();
    }
    public Student update(Student student) {  //возвращаем старую версию студента
        Student oldStudent = read(student.getId()); //если такого нет - возникнет исключение
        repository.save(student);
        return oldStudent;
    }
    public Student delete(Long id) {  //возвращаем удаленного студента
        Student oldStudent = read(id); //если такого нет - возникнет исключение
        repository.deleteById(id);
        return oldStudent;
    }
    //public List<Student> readByAge(int age) {
    //    return students.values().stream().filter(s->s.getAge()==age).collect(Collectors.toList());
    //}
    //public Collection<Student> readAll() {
    //    return students.values();
    //}

}
