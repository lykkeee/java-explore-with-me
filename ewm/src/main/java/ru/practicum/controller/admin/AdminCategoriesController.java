package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryRequestDto;
import ru.practicum.model.category.CategoryResponseDto;
import ru.practicum.service.category.CategoryServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@AllArgsConstructor
public class AdminCategoriesController {

    private final CategoryServiceImpl categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto addCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        log.info("Запрос на добавление новой категории: {}", categoryRequestDto.getName());
        CategoryResponseDto response = categoryService.addCategory(categoryRequestDto);
        log.info("Категория добавлена: {}", response);
        return response;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("Запрос на удаление категории с id: {}", catId);
        categoryService.deleteCategory(catId);
        log.info("Категория удалена");
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDto updateCategory(@PathVariable Integer catId,
                                              @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        log.info("Запрос на обновление категории с id: {}", catId);
        CategoryResponseDto response = categoryService.updateCategory(catId, categoryRequestDto);
        log.info("Категория обновлена: {}", response);
        return response;
    }

}
