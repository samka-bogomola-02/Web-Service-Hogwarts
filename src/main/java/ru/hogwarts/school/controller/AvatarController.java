package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }
    @PostMapping(value = "/addition", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public long additionAvatar(@RequestParam("studentId")long studentId,
                               @RequestBody MultipartFile file) throws IOException {
        return avatarService.additionAvatar(studentId, file);
    }
    @GetMapping("/get/db")
    public ResponseEntity<byte[]> getAvatarDb(@RequestParam("studentId") long studentId) {
        Avatar avatar = avatarService.getAvatarDb(studentId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(avatar.getData());
    }
    @GetMapping("/get/local")
    public ResponseEntity<byte[]> getAvatar(@RequestParam("studentId") long studentId) throws IOException {
        Avatar avatar = avatarService.getAvatar(studentId);
        byte[] bytes = Files.readAllBytes(Path.of(avatar.getFilePath()));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(bytes);
    }
}
