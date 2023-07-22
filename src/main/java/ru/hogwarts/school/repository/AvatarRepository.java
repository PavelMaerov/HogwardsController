package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

@Repository
public interface AvatarRepository  extends JpaRepository<Avatar, Long> {
    //из-за того, что student_id не удалось сделать ключом,
    //приходится создавать дополнительный метод для поиска по student_id.
    //ключ не может быть примитивом
    Optional<Avatar> findByStudentId(Long studentId);
}
