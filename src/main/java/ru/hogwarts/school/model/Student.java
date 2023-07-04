package ru.hogwarts.school.model;

import javax.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;
    @ManyToOne //владелец связи - сторона Много
    //В нашем случае аннотация JoinColumn не нужна.
    //Со стороны Много (student) создастся поле с именем по умолчанию
    // = имя свойства этого класса (faculty)_ключевое поле со стороны Один
    //@JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student(Long id, String name, int age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }
    public Student() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}
