package com.fundmate.api.unit.category;

import com.fundmate.api.dto.request.CategoryRequest;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.mapper.CategoryMapper;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.BudgetRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ScheduledTransactionRepository scheduledTransactionRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryRequest categoryRequest;
    private Category category;
    private CategoryResponse categoryResponse;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName("Food");
        categoryRequest.setIcon("food-icon");

        category = new Category();
        category.setId(1L);
        category.setCategoryName("Food");
        category.setIcon("food-icon");
        category.setUser(user);

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setCategoryName("Food");
        categoryResponse.setIcon("food-icon");
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void createCategory_ShouldCreateAndReturnCategory() {
        setupSecurityContext();
        when(categoryMapper.toEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(categoryRequest);

        assertNotNull(result);
        assertEquals(categoryResponse.getId(), result.getId());
        assertEquals(categoryResponse.getCategoryName(), result.getCategoryName());
        verify(categoryRepository).save(category);
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        setupSecurityContext();
        when(categoryRepository.findByUserId(1L)).thenReturn(Arrays.asList(category));
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryResponse, result.get(0));
        verify(categoryRepository).findByUserId(1L);
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        setupSecurityContext();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(categoryResponse, result);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_WithInvalidId_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                categoryService.getCategoryById(1L));
    }

    @Test
    void getCategoryById_WithUnauthorizedAccess_ShouldThrowException() {
        setupSecurityContext();
        User otherUser = new User();
        otherUser.setId(2L);
        category.setUser(otherUser);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResponseStatusException.class, () ->
                categoryService.getCategoryById(1L));
    }

    @Test
    void deleteCategory_WithTransactions_ShouldThrowException() {
        setupSecurityContext();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () ->
                categoryService.deleteCategory(1L));

        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_WithBudgets_ShouldThrowException() {
        setupSecurityContext();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);
        when(budgetRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () ->
                categoryService.deleteCategory(1L));

        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_WithScheduledTransactions_ShouldThrowException() {
        setupSecurityContext();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);
        when(budgetRepository.existsByCategoryId(1L)).thenReturn(false);
        when(scheduledTransactionRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () ->
                categoryService.deleteCategory(1L));

        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_WithNoRelations_ShouldDelete() {
        setupSecurityContext();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategoryId(1L)).thenReturn(false);
        when(budgetRepository.existsByCategoryId(1L)).thenReturn(false);
        when(scheduledTransactionRepository.existsByCategoryId(1L)).thenReturn(false);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }
}