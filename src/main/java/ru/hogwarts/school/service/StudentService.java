package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student) { //возвращаем нового студента c новым id
        logger.info("Was invoked method 'create' for student - {} ", student);
        Student newStudent = new Student(0L, student.getName(), student.getAge(), null);
        return repository.save(newStudent);
    }
    public Student read(Long id) {
        logger.info("Was invoked method 'read' for student with id =  " + id);
        Optional<Student> student = repository.findById(id);
        if (student.isEmpty()){
            logger.error("There is not student with id = {}", id);
            throw new NotFoundException("студент", id);
        }

        return student.get();
    }
    public Student update(Student student) {  //возвращаем старую версию студента
        logger.info("Was invoked method 'update' for student - {}", student);
        Student oldStudent = read(student.getId()); //если такого нет - возникнет исключение
        //Интересный эффект: и read и save возвращают указатель на одно и то же место.
        //Это значит, что сохраняя результат read, мы не сохраняем ничего. save все перепишет
        oldStudent = new Student(oldStudent.getId(), oldStudent.getName(), oldStudent.getAge(), oldStudent.getFaculty());
        repository.save(student);
        return oldStudent;
    }
    public Student delete(Long id) {  //возвращаем удаленного студента
        logger.info("Was invoked method 'delete' for student with id = {}", id);
        Student oldStudent = read(id); //если такого нет - возникнет исключение
        repository.deleteById(id);
        return oldStudent;
    }
    public Set<Student> readByAge(int age) {
        logger.info("Was invoked method 'readByAge' with age = {}", age);
        return repository.findByAge(age);
    }
    public Set<Student> readByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method 'readByAgeBetween' with minAge = {}  and maxAge = {}", minAge, maxAge);
        return repository.findByAgeBetween(minAge, maxAge);
    }
    public Set<Student> readAll() {
        logger.info("Was invoked method 'readAll'");
        return new HashSet<>(repository.findAll());
    }
    public int studentCount() {
        logger.info("Was invoked method 'studentCount'");
        return repository.studentCount();
    }
    public double avgAge() {
        logger.info("Was invoked method 'avgAge'");
        return repository.avgAge();
    }
    public Set<Student> Last5() {
        logger.info("Was invoked method 'Last5'");
        return repository.Last5();
    }
    public List<String> allNamesA() {
        //отсортированный список имен всех студентов в верхнем регистре, чье имя начинается с буквы А
        return repository.findAll().stream()
                .map(Student::getName)
                .filter(s->s.matches("[a,A].*")) //наверно startsWith быстрее, но для эксперимента сделал регулярное выражение
                .map(String::toUpperCase) //в верхний регистр перевожу после фильтрации, чтобы не переводить ненужные
                .sorted()
                .collect(Collectors.toList());
    }
    public double avgAgeByStream() {
        return repository.findAll().stream()
                .mapToInt(Student::getAge)
                .average().orElse(0);
    }
    public long sumMillion() {  //int переполнится от такой суммы и даже не выдаст ошибку
        long start = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        long finish = System.currentTimeMillis();
        logger.info("Execution time of 'sumMillion' method = {} мс",finish-start);
        return sum;
    }
    public long sumMillionParallel() {
        long start = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        long finish = System.currentTimeMillis();
        logger.info("Execution time of 'sumMillionParallel' method = {} мс",finish-start);
        return sum;
        //после распараллеливания получил ухудшение примерно в 2 раза
    }

    //печатает из списка студентов quantity имен со смещением offset от начала
    private void printPartOfList(List<Student> list, int offset, int quantity) {
        Iterator<Student> iterator= list.listIterator();
        for (int i = 0; i < offset; i++) iterator.next();   //сдвигаемся на offset позиции
        for (int i = 0; i < quantity; i++) {                //печатаем quantity позиции
            System.out.println(iterator.next().getName());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //тоже самое, но метод недоступен двум потокам сразу
    private synchronized void printPartOfListSynchronized(List<Student> list, int offset, int quantity) {
        printPartOfList(list, offset, quantity);
    }
    public void printNamesNotSynchronized(){
        logger.info("Execution method 'printNamesNotSynchronized'");
        List<Student> list = repository.findAll();
        //вариант запуска 1 - лямбда - для 3его и 4го студента
        new Thread(()->printPartOfList(list,2,2)).start();
        //вариант запуска 2 - анонимный класс - для 5го и 6го студента
        new Thread(){
            @Override
            public void run() {
                printPartOfList(list,4,2);
            }
        }.start();

        //порядок запуска потоков в задании не оговорен
        //поэтому запустим 2 дополнительных вместе с основным, чтобы все 3 работали одновременно
        //основной поток
        printPartOfList(list,0,2);

        //Порядок имен студентов в консоли вывода. Получилось много вариантов. Вот один из них.
        //1й - Вывод первого имени из основного потока опередил все дополнительные, хотя метод был запущен последним. Нет смещения по итератору?
        //3й
        //5й
        //2й
        //6й - При выводе второго имени второй поток опередил первый
        //4й
    }

    public void printNamesSynchronized() {
        logger.info("Execution method 'printNamesSynchronized'");
        List<Student> list = repository.findAll();
        new Thread(()->printPartOfListSynchronized(list,2,2)).start();
        new Thread(()->printPartOfListSynchronized(list,4,2)).start();
        printPartOfListSynchronized(list,0,2);

        //Порядок имен студентов в консоли вывода. Получилось много вариантов. Вот один из них.
        //3й
        //4й
        //1й - Основной поток, даже стоя в очереди за доступом к методу, все равно отодвинул второй поток после себя. Недостойное поведение! :)
        //2й
        //5й
        //6й

        //Я конечно попробовал запустить метод из основного потока первым и
        //получил (как по заданию) порядок вывода, совпадающий со списком
        //Но этот случай менее интересен,
        //т.к. основной поток не образует конкуренции дополнительным.
        //Ведь дополнительные потоки стартуют только после окончания метода из основного


    }
}
