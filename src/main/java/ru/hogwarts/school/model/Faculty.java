package ru.hogwarts.school.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Faculty {
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
}
