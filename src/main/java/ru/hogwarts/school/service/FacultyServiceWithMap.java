package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Service
public class FacultyServiceWithMap {
    private long countId = 0;
    private final Map<Long, Faculty> faculties = new HashMap<>();

    public Faculty create(Faculty faculty) { //возвращаем новый факультет c новым id
        Faculty newFaculty = new Faculty(++countId, faculty.getName(), faculty.getColor());
        faculties.put(countId, newFaculty);
        return newFaculty;
    }
    public Faculty read(Long id) {
        Faculty faculty = faculties.get(id);
        if (faculty == null) throw new NotFoundException("факультет", id);
        return faculty;
    }
    public Faculty update(Faculty faculty) {  //возвращаем старую версию факультета
        Faculty oldFaculty = faculties.replace(faculty.getId(), faculty);
        if (oldFaculty == null) throw new NotFoundException("факультет", faculty.getId());
        return oldFaculty;
    }
    public Faculty delete(Long id) {  //возвращаем удаленный факультет
        Faculty faculty = faculties.remove(id);
        if (faculty == null) throw new NotFoundException("факультет", id);
        return faculty;
    }
    public List<Faculty> readByColor(String color) {
        return faculties.values().stream().filter(f->f.getColor().equals(color)).collect(Collectors.toList());
    }
}
