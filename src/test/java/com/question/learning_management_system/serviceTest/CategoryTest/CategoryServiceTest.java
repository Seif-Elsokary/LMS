package com.question.learning_management_system.serviceTest.CategoryTest;

import com.question.learning_management_system.entity.Category;
import com.question.learning_management_system.exception.CategoryNotFoundException;
import com.question.learning_management_system.repository.CategoryRepository;
import com.question.learning_management_system.service.CategoryService.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder().id(1L).name("Programming").build();
    }

    @Test
    public void testCreateCategory_Success() {
        when(categoryRepository.existsByNameIgnoreCase(category.getName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testCreateCategory_AlreadyExists() {
        when(categoryRepository.existsByNameIgnoreCase(category.getName())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(category);
        });

        assertEquals("Category with name 'Programming' already exists.", exception.getMessage());
        verify(categoryRepository, times(0)).save(category);
    }

    @Test
    public void testGetAllCategories() {
        Category category1 = Category.builder().id(1L).name("Programming").build();
        Category category2 = Category.builder().id(2L).name("Math").build();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        var categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, times(0)).delete(any());
    }
}
