package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @RequestMapping
    public String helloHogwarts() {
        return "Welcome to the School of Witchcraft and Wizardry!";
    }
}
