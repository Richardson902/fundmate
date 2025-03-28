package com.fundmate.api.mapper;

import com.fundmate.api.dto.request.ScheduledTransactionRequest;
import com.fundmate.api.dto.response.ScheduledTransactionResponse;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.ScheduledTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduledTransactionMapper {
    ScheduledTransactionResponse toResponse(ScheduledTransaction scheduledTransaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "accountId", target = "account")
    ScheduledTransaction toEntity(ScheduledTransactionRequest scheduledTransactionRequest);

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
