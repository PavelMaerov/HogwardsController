package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Set;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return service.create(faculty);
    }
    @GetMapping(params="id")
    public Faculty read(@RequestParam Long id) {
        return service.read(id);
    }
    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return service.update(faculty);
    }
    @DeleteMapping
    public Faculty delete(@RequestParam Long id) {
        return service.delete(id);
    }

    @GetMapping(params = "color")
    public Set<Faculty> readByColor(@RequestParam String color) {
        return service.readByColor(color);
   }
    /*
    @GetMapping(params = {"color","name"})
    //вариант, когда оба параметра должны быть заданы, но могут быть c пустым значением
    public List<Faculty> readBynameOrColor(@RequestParam(required = false) String color,
                                           @RequestParam(required = false) String name) {
        if (color != null && !color.isEmpty()) {
            return service.readByColorIgnoreCase(color);
        }
        if (name != null && !name.isEmpty()) {
            return service.readByNameIgnoreCase(name);
        }
        return Collections.emptyList();
    }
    */
    @GetMapping(params = "nameOrColor")  //вариант, когда по одному параметру ищем в обоих полях
    public Set<Faculty> readByNameOrColor(@RequestParam String nameOrColor) {
         return service.readByNameIgnoreCaseOrColorIgnoreCase(nameOrColor);
    }
    @GetMapping
    public Set<Faculty> readAll() {
        return service.readAll();
    }
    @GetMapping(params = "studentsOfFaculty")
    public Set<Student> readStudentsOfFaculty(@RequestParam(name = "studentsOfFaculty") Long facultyId) {
        return service.read(facultyId).getStudents();
    }

    @GetMapping("longestName")
    public String longestName() {
        return service.longestName();
    }
}
