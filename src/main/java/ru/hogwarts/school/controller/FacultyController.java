package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

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
    public List<Faculty> readByAge(@RequestParam String color) {
        return service.readByColor(color);
   }
    @GetMapping
    public Collection<Faculty> readAll() {
        return service.readAll();
    }

}
