package ru.hogwarts.school.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentInterface;

import java.util.Collection;
import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByNameIgnoreCase(String name);
//    List<Student> getStudentsByName(String name);
    Collection<Student> findByAge(Integer age);
    Collection<Student> findByNameContainsIgnoreCase(String part);

    @Query(value = "SELECT COUNT(*) FROM Student", nativeQuery = true)
    long countAllStudents();
    @Query(value = "SELECT AVG(age) FROM Student", nativeQuery = true)
    Double getAverageAge();
    @Query(value = "SELECT * FROM Student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();


}
