package com.question.learning_management_system.service.CategoryService;

import com.question.learning_management_system.entity.Category;
import com.question.learning_management_system.exception.CategoryNotFoundException;
import com.question.learning_management_system.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        boolean exists = categoryRepository.existsByNameIgnoreCase(category.getName());
        if (exists) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}
