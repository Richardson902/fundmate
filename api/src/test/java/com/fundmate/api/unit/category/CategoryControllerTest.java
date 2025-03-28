package com.fundmate.api.unit.category;

import com.fundmate.api.controller.CategoryController;
import com.fundmate.api.dto.request.CategoryRequest;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName("Food");
        categoryRequest.setIcon("food-icon");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setCategoryName("Food");
        categoryResponse.setIcon("food-icon");
    }

    @Test
    void createCategory_ShouldReturnCreatedResponse() {
        when(categoryService.createCategory(any(CategoryRequest.class)))
                .thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.createCategory(categoryRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoryResponse, response.getBody());
        verify(categoryService).createCategory(categoryRequest);
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        List<CategoryResponse> categories = Arrays.asList(categoryResponse);
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<CategoryResponse>> response = categoryController.getAllCategories();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(categoryService).getAllCategories();
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryResponse, response.getBody());
        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void deleteCategoryById_ShouldReturnNoContent() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoryService).deleteCategory(1L);
    }
}