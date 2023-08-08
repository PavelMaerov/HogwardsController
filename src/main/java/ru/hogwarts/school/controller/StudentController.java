package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return service.create(student);
    }

    @GetMapping(params = "id")
    public Student read(@RequestParam Long id) {
        return service.read(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return service.update(student);
    }

    @DeleteMapping
    public Student delete(@RequestParam Long id) {
        return service.delete(id);
    }

    @GetMapping(params = "age")
    public Set<Student> readByAge(@RequestParam int age) {
        return service.readByAge(age);
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public Set<Student> readByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return service.readByAgeBetween(minAge, maxAge);
    }

    @GetMapping
    public Set<Student> readAll() {
        return service.readAll();
    }

    @GetMapping(params = "facultyOfStudent")
    public Faculty readFacultyOfStudent(@RequestParam(name = "facultyOfStudent") Long studentId) {
        return service.read(studentId).getFaculty();
    }

    @GetMapping("count")
    public int studentCount() {
        return service.studentCount();
    }
    @GetMapping("avgAge")
    public double avgAge() {
        return service.avgAge();
    }
    @GetMapping("last5")
    public Set<Student> Last5() {
        return service.Last5();
    }

    @GetMapping("allNamesA")
    public List<String> allNames() {
        return service.allNamesA();
    }
    @GetMapping("avgAgeByStream")
    public double avgAgeByStream() {
        return service.avgAgeByStream();
    }
    @GetMapping("sumMillion")
    public long sumMillion() {
        return service.sumMillion();
    }
    @GetMapping("sumMillionParallel")
    public long sumMillionParallel() {
        return service.sumMillionParallel();
    }
}
