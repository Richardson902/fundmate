package com.fundmate.api.mapper;

import com.fundmate.api.dto.request.BudgetRequest;
import com.fundmate.api.dto.response.BudgetResponse;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Budget;
import com.fundmate.api.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BudgetMapper {
    @Mapping(target = "spentAmount", ignore = true)
    @Mapping(target = "residualAmount", ignore = true)
    @Mapping(target = "completionPercentage", ignore = true)
    BudgetResponse toResponse(Budget budget);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "accountId", target = "account")
    Budget toEntity(BudgetRequest request);

    default Category mapIdToCategory(Long id) {
        if (id == null) return null;
        Category category = new Category();
        category.setId(id);
        return category;
    }

    default Account mapIdToAccount(Long id) {
        if (id == null) return null;
        Account account = new Account();
        account.setId(id);
        return account;
    }
}
