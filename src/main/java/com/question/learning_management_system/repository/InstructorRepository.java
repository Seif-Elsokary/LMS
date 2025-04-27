package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {


    boolean existsByEmail(String email);

    Instructor findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
