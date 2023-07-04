package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty create(Faculty faculty) { //возвращаем новый факультет c новым id
        Faculty newFaculty = new Faculty(0L, faculty.getName(), faculty.getColor());
        return repository.save(newFaculty);
    }
    public Faculty read(Long id) {
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isEmpty()) throw new NotFoundException("факультет", id);
        return faculty.get();
    }
    public Faculty update(Faculty faculty) {  //возвращаем старую версию факультета
        Faculty oldFaculty = read(faculty.getId()); //если такого нет - возникнет исключение
        //Интересный эффект: и read и save возвращают указатель на одно и то же место.
        //Это значит, что сохраняя результат read, мы не сохраняем ничего. save все перепишет
        oldFaculty = new Faculty(oldFaculty.getId(), oldFaculty.getName(), oldFaculty.getColor());
        repository.save(faculty);
        return oldFaculty;
    }
    public Faculty delete(Long id) {  //возвращаем удаленный факультет
        Faculty oldFaculty = read(id); //если такого нет - возникнет исключение
        repository.deleteById(id);
        return oldFaculty;
    }
    public Set<Faculty> readByColor(String color) {
        return repository.findByColor(color);
    }

    //для варианта, когда оба параметра должны быть заданы, но могут быть c пустым значением
    public Set<Faculty> readByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }
    public Set<Faculty> readByColorIgnoreCase(String color) {
        return repository.findByColorIgnoreCase(color);
    }
    //для варианта, когда по одному параметру ищем в обоих полях
    public Set<Faculty> readByNameIgnoreCaseOrColorIgnoreCase(String nameOrColor) {
        return repository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }
    public Set<Faculty> readAll() {
        return new HashSet<>(repository.findAll());
    }
}
