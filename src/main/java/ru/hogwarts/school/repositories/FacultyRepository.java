package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
@Repository
public interface FacultyRepository extends JpaRepository<Faculty,Long>{
    Faculty findByNameIgnoreCase(String name);
    Collection<Faculty> findByColorContainsIgnoreCase(String color);
    Collection<Faculty> findByNameContainsIgnoreCase(String part);
}
