package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Optional;

@Service
public class FacultyService {
    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty create(Faculty faculty) { //возвращаем нового студента c новым id
        Faculty newFaculty = new Faculty(0L, faculty.getName(), faculty.getColor());
        return repository.save(newFaculty);
    }
    public Faculty read(Long id) {
        Optional<Faculty> faculty = repository.findById(id);
        if (faculty.isEmpty()) throw new NotFoundException("студент", id);
        return faculty.get();
    }
    public Faculty update(Faculty faculty) {  //возвращаем старую версию студента
        Faculty oldFaculty = read(faculty.getId()); //если такого нет - возникнет исключение
        repository.save(faculty);
        return oldFaculty;
    }
    public Faculty delete(Long id) {  //возвращаем удаленного студента
        Faculty oldFaculty = read(id); //если такого нет - возникнет исключение
        repository.deleteById(id);
        return oldFaculty;
    }
    //public List<Faculty> readByAge(int age) {
    //    return facultys.values().stream().filter(s->s.getAge()==age).collect(Collectors.toList());
    //}
    //public Collection<Faculty> readAll() {
    //    return facultys.values();
    //}

}
