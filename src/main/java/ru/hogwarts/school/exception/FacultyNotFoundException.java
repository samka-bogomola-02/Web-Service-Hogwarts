package ru.hogwarts.school.exception;

public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(long id) {
        super("Faculty with id " + id + " not found");
    }
}
