package com.fundmate.api.mapper;

import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.dto.response.TransactionResponse;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "accountId", target = "account")
    Transaction toEntity(TransactionRequest transactionRequest);

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
