package ru.practicum.controller.privacy_public;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryResponseDto;
import ru.practicum.service.category.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@AllArgsConstructor
public class CategoriesController {

    private final CategoryServiceImpl categoryService;

    @GetMapping
    public List<CategoryResponseDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка категорий");
        List<CategoryResponseDto> response = categoryService.getCategories(from, size);
        log.info("Список получен");
        return response;
    }

    @GetMapping("/{catId}")
    public CategoryResponseDto getCategory(@PathVariable Integer catId) {
        log.info("Запрос на получение категории с id: {}", catId);
        CategoryResponseDto response = categoryService.getCategory(catId);
        log.info("Категория получена: {}", response);
        return response;
    }
}
