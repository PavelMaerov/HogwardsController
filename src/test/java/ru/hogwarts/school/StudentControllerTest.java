package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers=StudentController.class) //если не указать аргумент,
// то при тестировании этого контроллера будут создаваться и другие контроллеры, например AvatarController
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository repository;
    //хотя servis и controller не используются, все равно надо создавать эти поля
    @SpyBean
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    @Test
    //не понял, почему авторы mockMvc.perform выбрасывают нам необработанное исключение,
    //которое к тому же я и сам не знаю, как обрабатывать и поэтому сам пробрасываю наружу.
    //Им что, было лень обработать? Почему они так берегут свой код от обработки ошибок?
    public void testCreate() throws Exception{
        long id = 1;
        String name = "Bob";
        int age = 30;

        //то, что посылаем в контроллер в теле запроса, без id
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        //для мока
        Student student = new Student(id, name, age, null);
        when(repository.save(any())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")                 //действие
                        .content(studentObject.toString())          //тело запроса
                        .contentType(MediaType.APPLICATION_JSON)    //тип тела
                        .accept(MediaType.APPLICATION_JSON))        //тип ответа наверно?
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }
    @Test
    public void testRead() throws Exception {
        long id = 1;
        String name = "Bob";
        int age = 30;

        //для мока
        Student student = new Student(id, name, age, null);
        when(repository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student?id=123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testReadWhenNotFound() throws Exception {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        long notExistingId = 123;
        mockMvc.perform(MockMvcRequestBuilders.get("/student?id="+notExistingId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Отсутствует студент c id="+notExistingId));
    }

    @Test
    public void testUpdate() throws Exception{
        long id = 1;
        String oldName = "oldName";
        int oldAge = 20;
        String newName = "newName";
        int newAge = 30;
        
        //то, что посылаем в контроллер в теле запроса
        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", newName);
        studentObject.put("age", newAge);

        //для мока findById
        Student oldStudent = new Student(id, oldName, oldAge, null);
        when(repository.findById(any())).thenReturn(Optional.of(oldStudent));

        //для мока save
        Student newStudent = new Student(id, newName, newAge, null);
        when(repository.save(any())).thenReturn(newStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")                  //действие
                        .content(studentObject.toString())          //тело запроса
                        .contentType(MediaType.APPLICATION_JSON))   //тип тела
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(oldName) )
                .andExpect(jsonPath("$.age").value(oldAge));
    }

    @Test
    public void testUpdateWhenNotFound() throws Exception {
        long notExistingId = 123;

        //то, что посылаем в контроллер в теле запроса.
        //Вообще, можно послать все что угодно.
        //Реально id используется для findById, но в тесте результат замокан и id не важно
        //остальное тело используется для новых полей, но в тесте реально ничего не сохраняется и что посылаем не важно.
        //Важно, чтобы контроллер нашел в теле студента
        JSONObject studentObject = new JSONObject();
        studentObject.put("id", notExistingId);
        studentObject.put("name", "");
        studentObject.put("age", -1);

        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/student")                  //действие
                    .content(studentObject.toString())          //тело запроса
                    .contentType(MediaType.APPLICATION_JSON))   //тип тела
                .andExpect(status().isNotFound())
                .andExpect(content().string("Отсутствует студент c id="+notExistingId));
    }

    @Test
    public void testDelete() throws Exception{
        long id = 1;
        String oldName = "oldName";
        int oldAge = 20;

        //для мока findById
        Student oldStudent = new Student(id, oldName, oldAge, null);
        when(repository.findById(any())).thenReturn(Optional.of(oldStudent));

        mockMvc.perform(MockMvcRequestBuilders.delete("/student?id=11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(oldName) )
                .andExpect(jsonPath("$.age").value(oldAge));
    }

    @Test
    public void testDeleteWhenNotFound() throws Exception {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        long notExistingId = 123;
        mockMvc.perform(MockMvcRequestBuilders.delete("/student?id="+notExistingId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Отсутствует студент c id="+notExistingId));
    }

    Student[] expected = {  //для создания мока и для expected
            //массив уже отсортирован по студенту
            new Student(1L,"Student1",15, null),
            new Student(2L,"Student2",16, null),
            new Student(3L,"Student3",17, null)};
    @Test
    public void testReadByAge() throws Exception {
        Set<Student> setStudents=new HashSet<>();
        Collections.addAll(setStudents, expected);
        when(repository.findByAge(anyInt())).thenReturn(setStudents);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonExpected = objectMapper.writeValueAsString(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/student?age=123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(content().json(jsonExpected));
    }

    @Test
    public void testReadAll() throws Exception {
        List<Student> listStudents = List.of(expected);
        when(repository.findAll()).thenReturn(listStudents);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonExpected = objectMapper.writeValueAsString(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(content().json(jsonExpected));
    }
    @Test
    public void testReadFacultyOfStudent() throws Exception {
        long id = 1;
        String name = "myFaculty";
        String color= "blue";

        //для мока
        Student student = new Student(0L, "", 0, new Faculty(id, name, color));
        when(repository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student?id=123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.faculty.id").value(id))
                .andExpect(jsonPath("$.faculty.name").value(name))
                .andExpect(jsonPath("$.faculty.color").value(color));
    }
}

