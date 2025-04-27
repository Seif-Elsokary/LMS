package com.question.learning_management_system.service.CategoryService;

import com.question.learning_management_system.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    void deleteCategory(Long id);
}
