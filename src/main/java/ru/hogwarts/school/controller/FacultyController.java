package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    Faculty create(@RequestBody Faculty faculty) {
        return service.create(faculty);
    }
    @GetMapping(params="id")
    Faculty read(@RequestParam Long id) {
        return service.read(id);
    }
    @PutMapping
    Faculty update(@RequestBody Faculty faculty) {
        return service.update(faculty);
    }
    @DeleteMapping
    Faculty delete(@RequestParam Long id) {
        return service.delete(id);
    }

    @GetMapping(params = "color")
    List<Faculty> readByAge(@RequestParam String color) {
        return service.readByColor(color);
    }

}
