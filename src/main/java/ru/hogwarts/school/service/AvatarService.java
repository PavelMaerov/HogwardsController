package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    StudentService studentService;
    AvatarRepository avatarRepository;
    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        //Взял этот оператор из шпаргалки.
        //Непонятно, зачем нужно искать данные студента, чтобы сохранить его аватар.
        //Возможно, чтобы построить структуру, где в аватар встроен студент.
        //А если я в аватар вложу студента с реквизитами, отличными от базы,
        //они у студента при сохранении аватара обновятся?
        Student student = studentService.read(studentId);  //если нет, возникнет исключение
        Path filePath = Path.of(avatarsDir, studentId + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Optional<Avatar> avatarOptional = avatarRepository.findByStudentId(studentId);
        Avatar avatar = avatarOptional.orElseGet(Avatar::new);
        //У нового объекта в id будет null и репозиторий его создаст в базе
        //у старого - id сохранится после извлечения его из базы
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(long studentId) {
        Optional<Avatar> avatar = avatarRepository.findByStudentId(studentId);
        if (avatar.isEmpty()) throw new NotFoundException("аватар студента", studentId);
        return avatar.get();
    }

    public List<Avatar> readPage(int page, int size) {
        return avatarRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}
