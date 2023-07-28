package ru.hogwarts.school.model;

import javax.persistence.*;

@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    //@Lob при этой аннотации - ошибка: столбец "data" имеет тип bytea, а выражение - bigint
    //Если файл больше 1M, то будет ошибка
    private byte[] data;
    @OneToOne //создастся поле student_id. Не смог сделать его ключом. Хотя в базе это можно сделать без проблем
    //Если сделать это поле ключом, то контроллер не запускается, т.к. требуется сериализация ключа.
    //Не понял толком зачем она нужна и что это такое
    private Student student;
    //также попробовал в качестве ключа поле student_id типа long.
    //Но на него не ставится отношение OneToOne. Со стороны внешнего ключа обязательно должен быть объект

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
