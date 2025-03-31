package com.fundmate.api.mapper;

import com.fundmate.api.dto.request.CategoryRequest;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
//    @Mapping(target = "user", ignore = true)
    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);
}
