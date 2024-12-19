package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
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
        logger.info("Вызван метод для добавления изображения для студента с ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Студент с ID {} не найден", studentId);
                    return new StudentNotFoundException(studentId);
                });

        Path path = saveAvatarLocal(file);
        logger.debug("Аватар успешно сохранен по пути: {}", path);

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
                        logger.info("Старый аватар удален для студента с ID: {}", studentId);
                    } catch (IOException e) {
                        logger.error("Ошибка при удалении старого аватара для студента с ID: {}", studentId, e);
                        throw new RuntimeException(e);
                    }
                    avatar.setId(x.getId());
        });
        long avatarId = avatarRepository.save(avatar).getId();
        logger.info("Новый аватар добавлен с ID: {}", avatarId);
        return avatarId;
    }
    private Path saveAvatarLocal(MultipartFile file) throws IOException {
        createDirectoryIfNotExists();
        if (file.getOriginalFilename() == null) {
            logger.error("Некорректное имя изображения");
            throw new RuntimeException("Некорректное имя изображения");
        }
        Path path = Path.of(pathDir.toString(), UUID.randomUUID() + getExtension(file.getOriginalFilename()));

        Files.write(path, file.getBytes());
        logger.debug("Изображение сохранено в локальной файловой системе: {}", path);
        return path;
    }
    private String getExtension(String path) {
        return path.substring(path.lastIndexOf("."));
    }
    private void createDirectoryIfNotExists() throws IOException{
        if (Files.notExists(pathDir)) {
        Files.createDirectory(pathDir);
            logger.info("Создана директория для хранения изображений: {}", pathDir);
        } else {
            logger.debug("Директория уже существует: {}", pathDir);
        }
    }

    @Transactional
    @Override
    public Avatar getAvatarDb(long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Аватар не найден для студента с ID: {}", studentId);
                    return new AvatarNotFoundException();
                });
    }

    @Transactional
    @Override
    public Avatar getAvatar(long studentId) throws IOException {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Аватар не найден для студента с ID: {}", studentId);
                    return new AvatarNotFoundException();
                });
    }

    @Override
    public Page<Avatar> getAllAvatars(Pageable pageable) {
        logger.debug("Получение всех аватаров с пагинацией: {}", pageable);
        return avatarRepository.findAll(pageable);
    }
}
