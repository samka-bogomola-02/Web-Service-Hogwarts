package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty,Long>{
    Faculty findByNameIgnoreCase(String name);
    Collection<Faculty> findByColorContainsIgnoreCase(String color);
    Collection<Faculty> findByNameContainsIgnoreCase(String part);
}
