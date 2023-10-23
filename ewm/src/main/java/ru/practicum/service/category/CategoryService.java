package ru.practicum.service.category;

import ru.practicum.model.category.CategoryRequestDto;
import ru.practicum.model.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto);

    void deleteCategory(Integer catId);

    CategoryResponseDto updateCategory(Integer catId, CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> getCategories(Integer from, Integer size);

    CategoryResponseDto getCategory(Integer catId);

}
