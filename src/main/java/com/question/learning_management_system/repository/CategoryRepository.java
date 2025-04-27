package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findByNameIgnoreCase(String categoryName);
}
