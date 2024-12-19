package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
public class InfoController {
    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public String getPort() {
        return String.valueOf(port);
    }

    @GetMapping("/sum")
    public ResponseEntity<Integer> getSum() {
        int sum = IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
        return ResponseEntity.ok(sum);
    }
}
