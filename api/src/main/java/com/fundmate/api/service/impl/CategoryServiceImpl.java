package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.CategoryRequest;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.mapper.CategoryMapper;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.BudgetRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryMapper categoryMapper;
    private final ScheduledTransactionRepository scheduledTransactionRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, TransactionRepository transactionRepository, BudgetRepository budgetRepository, CategoryMapper categoryMapper, ScheduledTransactionRepository scheduledTransactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.categoryMapper = categoryMapper;
        this.scheduledTransactionRepository = scheduledTransactionRepository;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        category.setUser(getCurrentUser());
        Category savedcategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedcategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByUserId(getCurrentUser().getId()).stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (!category.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return categoryMapper.toResponse(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (!category.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (transactionRepository.existsByCategoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete category as it is used in transactions");
        }

        if (budgetRepository.existsByCategoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete category as it is used in budgets");
        }

        if (scheduledTransactionRepository.existsByCategoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete category as it is used in scheduled transactions");

        }

        categoryRepository.deleteById(id);
    }
}
