package ru.hogwarts.school.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;

public interface AvatarServiceInterface {
    long additionAvatar(long studentId, MultipartFile file) throws IOException;
    Avatar getAvatarDb(long studentId);
    Avatar getAvatar(long studentId) throws IOException;
}
