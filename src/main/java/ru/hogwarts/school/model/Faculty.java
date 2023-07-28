package ru.hogwarts.school.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Faculty implements Comparable<Faculty>{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;

    //@OneToMany
    //@JoinColumn(name="myColumn") не нашел, чем плох такой вариант против создания дополнительной таблицы
    //Да, получается не двунаправленная связь, а две однонапрвленные. Но что в этом плохого?

    //Ниже канонический вариант создания двунаправленной связи
    @OneToMany(mappedBy = "faculty") //студенты выборочно отбираются (маппируются) по полю факультета, совпадающим с ключем из этого объекта
    @JsonIgnore
    private Set<Student> students;

    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.students=new HashSet<>();
    }

    public Faculty() {}

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {  //нужно только для тестирования
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty)) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public int compareTo(Faculty o) {
        return (name + " " + color).compareTo(o.getName() + " " + o.getColor());
    }

}
