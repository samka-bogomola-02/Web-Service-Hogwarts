package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/pageable") // пагинация
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение изображений",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(avatarService.getAllAvatars(pageable));
    }
}
