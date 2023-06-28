package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    Student create(@RequestBody Student student) {
        return service.create(student);
    }
    @GetMapping(params = "id")
    Student read(@RequestParam Long id) {
        return service.read(id);
    }
    @PutMapping
    Student update(@RequestBody Student student) {
        return service.update(student);
    }
    @DeleteMapping
    Student delete(@RequestParam Long id) {
        return service.delete(id);
    }

    //@GetMapping(params = "age")
    //List<Student> readByAge(@RequestParam int age) {
    //    return service.readByAge(age);
    //}
    //@GetMapping
    //Collection<Student> readAll() {
    //    return service.readAll();
    //}

}
