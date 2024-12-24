package ru.hogwarts.school.service;

//import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.util.Comparator;
import java.util.Optional;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvatarServiceTest {
    private static final String TEST_PATH = "src/test/images";
    AvatarRepository avatarRepository = Mockito.mock(AvatarRepository.class);
    StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    AvatarService avatarService = new AvatarService(
            avatarRepository,
            studentRepository,
            Path.of(TEST_PATH));

    @Test
    void additionAvatar() throws IOException {
        Student student = new Student(1L, "Oleg", 34);
        Avatar avatar = new Avatar();
        avatar.setId(nextLong(1, 1000));

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(avatarRepository.save(any(Avatar.class))).thenReturn(avatar);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "test",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);

        long actualId = avatarService.additionAvatar(1, multipartFile);
        assertThat(actualId).isEqualTo(avatar.getId());
        assertThat(Files.walk(Path.of(TEST_PATH))).hasSize(9);
    }

    @Test
    void getAvatarDb() {
        long studentId = 1L;
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        when(avatarRepository.findByStudentId(studentId)).thenReturn(Optional.of(avatar));

        Avatar actualAvatar = avatarService.getAvatarDb(studentId);

        assertThat(actualAvatar).isNotNull();
        assertThat(actualAvatar.getId()).isEqualTo(1L);
        verify(avatarRepository).findByStudentId(studentId);
    }

    @Test
    void getAvatarDb_NotFound() {
        long studentId = 1L;

        when(avatarRepository.findByStudentId(studentId)).thenReturn(Optional.empty());

        assertThrows(AvatarNotFoundException.class, () -> avatarService.getAvatarDb(studentId));
        verify(avatarRepository).findByStudentId(studentId);
    }

    @Test
    void getAvatar() throws IOException {
        long studentId = 1L;
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        when(avatarRepository.findByStudentId(studentId)).thenReturn(Optional.of(avatar));

        Avatar actualAvatar = avatarService.getAvatar(studentId);

        assertThat(actualAvatar).isNotNull();
        assertThat(actualAvatar.getId()).isEqualTo(1L);
        verify(avatarRepository).findByStudentId(studentId);
    }

    @Test
    void getAvatar_NotFound() {
        long studentId = 1L;

        when(avatarRepository.findByStudentId(studentId)).thenReturn(Optional.empty());

        assertThrows(AvatarNotFoundException.class, () -> avatarService.getAvatar(studentId));
        verify(avatarRepository).findByStudentId(studentId);
    }
}