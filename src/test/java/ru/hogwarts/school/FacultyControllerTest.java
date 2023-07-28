package ru.hogwarts.school;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    //В шпаргалке здесь создан контроллер, чтобы проверить, что он не null.
    //Реально к контроллеру я нигде не обращался, поэтому он не нужен.
    //Я посылал запросы к контролеру, который Spring создал на случайном порту
    //@Autowired
    //private FacultyController сontroller;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private FacultyRepository repository;

    @Test
    //в шпаргалке - public void testCreate() throws Exception {
    //не понял из лекции, зачем перебрасывать Exception наружу.
    //если в тесте возникает исключение, он должен прерваться и мы должны увидеть место и причину прерывания
    //throws Exception разрешает нам не обрабатывать исключения, а доверить это вызывающему методу.
    //Однако я хочу прервать тест и разбираться с причиной

    public void testCreate() {
        long id = 1;
        String name = "myFaculty";
        String color = "blue";

        //то, что должны получить, как результат запроса - ответ контроллерв
        //String expected = "{\"id\":"+id+",\"name\":\""+name+"\",\"color\":\""+color+"\"}"; вариант для сравнения строк
        JSONObject expected = new JSONObject();
        expected.put("id", (int)id); //конструктор faculty-мока требует тип long, поэтому переменная id имеет тип long
                                    //но тогда в expected возникает id=1L, а контроллер возвращает id=1, тест проваливается
                                    //удивительно! получается при преобразовании в JSON одного и того же объекта - разный синтаксис
                                    //для ликвидации буквы L пришлось изменить тип id в int
        expected.put("name", name);
        expected.put("color", color);

        //то, что посылаем в теле запроса, без id
        Faculty facultyIn = new Faculty();
        facultyIn.setName(name);
        facultyIn.setColor(color);

        //то, что получаем из мока-репозитория
        Faculty facultyMock = new Faculty(id, name, color);
        when(repository.save(any(Faculty.class))).thenReturn(facultyMock);

        //restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyIn, JSONObject.class)
        //должен возвращать JSON объект {"id":1,"name":"myFaculty","color":"blue"};
        JSONObject actual = restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyIn, JSONObject.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testRead() {
        long id = 2;
        String name = "yourFaculty";
        String color = "red";

        //то, что должны получить, как результат запроса - ответ контроллерв
        JSONObject expected = new JSONObject();
        expected.put("id", (int)id);
        expected.put("name", name);
        expected.put("color", color);

        //то, что получаем из мока-репозитория
        Faculty facultyMock = new Faculty(id, name, color);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(facultyMock));

        JSONObject actual =  restTemplate.getForObject("http://localhost:" + port + "/faculty?id=11", JSONObject.class);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testReadWhenNotFound(){
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        long notExistingId = 123;
        ResponseEntity<String> actual = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty?id="+notExistingId, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("Отсутствует факультет c id="+notExistingId);
    }
    @Test
    public void testUpdate() {
        long id = 3;
        String oldName = "myFaculty";
        String oldColor = "blue";
        String newName = "yourFaculty";
        String newColor = "red";

        //то, что должны получить в теле ответа на запрос - ответ контроллера. = expected
        Faculty oldFaculty = new Faculty(id, oldName, oldColor); //то же, что и facultyMockFindById

        //то, что посылаем в теле запроса
        Faculty newFaculty = new Faculty(id, newName, newColor);

        //то, что получаем из мока-репозитория
        Faculty facultyMockFindById = new Faculty(id, oldName, oldColor);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(facultyMockFindById));
        Faculty facultyMockSave = new Faculty(id, newName, newColor);
        when(repository.save(any(Faculty.class))).thenReturn(facultyMockSave);

        ResponseEntity<Faculty>  response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(newFaculty), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(oldFaculty);
    }
    @Test
    public void testUpdateWhenNotFound(){
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        long notExistingId = 123;
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(new Faculty(notExistingId,"","")), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Отсутствует факультет c id="+notExistingId);
    }
    @Test
    public void testDelete() {
        long id = 3;
        String oldName = "myFaculty";
        String oldColor = "blue";

        //то, что должны получить в теле ответа на запрос - ответ контроллера. = expected
        Faculty oldFaculty = new Faculty(id, oldName, oldColor); //то же, что и facultyMock

        //то, что получаем из мока-репозитория
        Faculty facultyMock = new Faculty(id, oldName, oldColor);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(facultyMock));
        //Я ожидал, что вызов незамоканного метода, даже возвращающего void, приводит к ошибке
        //Однако нет, не вызвывает
        //Замокать .deleteById нельзя, т.к. он возвращает void
        //when(repository.deleteById(any(Long.class))).thenReturn(void); Так нельзя

        //restTemplate.delete("http://localhost:" + port + "/faculty?id=11"); так работает, но ничего не возвращает
        ResponseEntity<Faculty>  response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?id=11",
                HttpMethod.DELETE, HttpEntity.EMPTY, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(oldFaculty);
    }
    @Test
    public void testDeleteWhenNotFound(){
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        long notExistingId = 123;
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?id="+notExistingId,
                HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Отсутствует факультет c id="+notExistingId);
    }

    Faculty[] expected = {  //для создания мока и для expected
            //массив уже отсортирован по факультету
            new Faculty(1L,"Faculty1","blue"),
            new Faculty(2L,"Faculty2","red"),
            new Faculty(3L,"Faculty3","green")};

    @Test
    public void testReadByColor() {
        //то, что должны получить, как результат запроса - ответ контроллера
        //Так не получается. метод wrap для Faculty возвращает null
        //JSONArray expected = new JSONArray(testFaculties);
        //Тогда сделаем expected=testFaculties. Будем сравнивать коллекции

        //то, что получаем из мока-репозитория. Возвращаемый Set будет не отсортирован
        Set<Faculty> setFaculties=new HashSet<>();
        Collections.addAll(setFaculties,expected);
        when(repository.findByColor(any(String.class))).thenReturn(setFaculties);

        //делаем запрос с любым цветом. Все равно вернется мок
        //так не работает. Не разобрался почему.
        //Cannot deserialize value of type `org.json.JSONArray` from Array value (token `JsonToken.START_ARRAY`)
        //JSONArray actual =  restTemplate.getForObject("http://localhost:" + port + "/faculty?color=black", JSONArray.class);
        Faculty[] actual =  restTemplate.getForObject(
                "http://localhost:" + port + "/faculty?color=black", Faculty[].class);
        Arrays.sort(actual);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testReadByNameOrColor() {
        //то, что получаем из мока-репозитория. Возвращаемый Set будет не отсортирован
        Set<Faculty> setFaculties=new HashSet<>();
        Collections.addAll(setFaculties, expected);
        when(repository.findByNameIgnoreCaseOrColorIgnoreCase(
                any(String.class),any(String.class))).thenReturn(setFaculties);

        Faculty[] actual =  restTemplate.getForObject(
                "http://localhost:" + port + "/faculty?nameOrColor=black", Faculty[].class);
        Arrays.sort(actual);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testReadAll() {
        //то, что получаем из мока-репозитория. Возвращаемый Set будет не отсортирован
        List<Faculty> listFaculties=new ArrayList<>();
        Collections.addAll(listFaculties, expected);
        when(repository.findAll()).thenReturn(listFaculties);

        Faculty[] actual =  restTemplate.getForObject(
                "http://localhost:" + port + "/faculty", Faculty[].class);
        Arrays.sort(actual);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testReadStudentsOfFaculty() {
        Student[] expected = {  //для создания мока и для expected
                //массив уже отсортирован по студенту
                new Student(1L, "Student1", 15, null),
                new Student(2L, "Student2", 16, null),
                new Student(3L, "Student3", 17, null)};

        //то, что получаем из мока-репозитория. Возвращаемый Set будет не отсортирован
        Set<Student> setStudents=new HashSet<>();
        Collections.addAll(setStudents, expected);
        Faculty facultyMock = new Faculty(0L, "", "");
        facultyMock.setStudents(setStudents);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(facultyMock));

        Student[] actual =  restTemplate.getForObject(
                "http://localhost:" + port + "/faculty?studentsOfFaculty=123", Student[].class);
        Arrays.sort(actual);
        assertThat(actual).isEqualTo(expected);
    }
}

