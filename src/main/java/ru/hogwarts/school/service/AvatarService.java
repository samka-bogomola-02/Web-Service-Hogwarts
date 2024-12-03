package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.interfaces.AvatarServiceInterface;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service

public class AvatarService implements AvatarServiceInterface {
    private final Path pathDir;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         @Value("${image.path}") Path pathDir) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.pathDir = pathDir;
    }

    @Override
    public long additionAvatar(long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        Path path = saveAvatarLocal(file);

        Avatar avatar = new Avatar(
                path.toString(),
                file.getSize(),
                file.getContentType(),
                file.getBytes(),
                student
        );
        avatarRepository.findByStudentId(studentId)
                .ifPresent((x) -> {
                    try {
                        Files.delete(Path.of(x.getFilePath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    avatar.setId(x.getId());
        });
        return avatarRepository.save(avatar).getId();
    }
    private Path saveAvatarLocal(MultipartFile file) throws IOException {
        createDirectoryIfNotExists();
        if (file.getOriginalFilename() == null) {
            throw new RuntimeException("Не корректное имя изображения");
        }
        Path path = Path.of(pathDir.toString(), UUID.randomUUID() + getExtension(file.getOriginalFilename()));

        Files.write(path, file.getBytes());
        return path;
    }
    private String getExtension(String path) {
        return path.substring(path.lastIndexOf("."));
    }
    private void createDirectoryIfNotExists() throws IOException{
        if (Files.notExists(pathDir)) {
        Files.createDirectory(pathDir);
        }
    }

    @Transactional
    @Override
    public Avatar getAvatarDb(long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(AvatarNotFoundException::new);
    }

    @Transactional
    @Override
    public Avatar getAvatar(long studentId) throws IOException {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(AvatarNotFoundException::new);
    }

    @Override
    public Page<Avatar> getAllAvatars(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }
}
