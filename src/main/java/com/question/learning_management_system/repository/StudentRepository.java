package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    Student findByPhoneNumber(String phoneNumber);

    List<Student> findByName(String name);
    List<Student> findByAge(int age);
    List<Student> findByNameAndAge(String name, int age);
    List<Student> findByGender(String gender);
    List<Student> findByAgeAndGender(int age, String gender);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByPhoneNumber(String phoneNumber);
}
