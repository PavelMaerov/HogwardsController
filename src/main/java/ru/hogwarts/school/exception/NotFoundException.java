package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    public String message;  //для эксперимента попробовал не перезаписывать getMessage, а создать свое поле
    public NotFoundException(String entity, long id) {
        message = "Отсутствует " + entity + " c id=" + id;
    }
}
