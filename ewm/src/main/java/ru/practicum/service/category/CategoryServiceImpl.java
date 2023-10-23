package ru.practicum.service.category;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictConditionsNotMetException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryRequestDto;
import ru.practicum.model.category.CategoryResponseDto;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.repository.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final ModelMapper mapper;

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = mapper.map(categoryRequestDto, Category.class);
        return mapper.map(categoryRepository.save(category), CategoryResponseDto.class);
    }

    @Override
    public void deleteCategory(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category with id=" + catId + " was not found"));
        if (!eventRepository.findByCategory(category).isEmpty()) {
            throw new ConflictConditionsNotMetException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryResponseDto updateCategory(Integer catId, CategoryRequestDto categoryRequestDto) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category with id=" + catId + " was not found"));
        Category category = mapper.map(categoryRequestDto, Category.class);
        category.setId(catId);
        return mapper.map(categoryRepository.save(category), CategoryResponseDto.class);
    }

    @Override
    public List<CategoryResponseDto> getCategories(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from, size)).toList();
        return categories.stream().map(category -> mapper.map(category, CategoryResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategory(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category with id=" + catId + " was not found"));
        return mapper.map(category, CategoryResponseDto.class);
    }
}
