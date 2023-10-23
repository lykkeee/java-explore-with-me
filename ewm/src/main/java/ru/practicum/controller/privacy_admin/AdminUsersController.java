package ru.practicum.controller.privacy_admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.user.UserRequestDto;
import ru.practicum.model.user.UserResponseDto;
import ru.practicum.service.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@AllArgsConstructor
public class AdminUsersController {
    private final UserServiceImpl userService;

    @GetMapping
    public List<UserResponseDto> getUsersAdmin(@RequestParam(required = false) List<Long> ids,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка пользователей с id: {}", ids);
        List<UserResponseDto> response = userService.getUsersAdmin(ids, from, size);
        log.info("Список пользователей получен");
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("Запрос на добавление пользователя: {}", userRequestDto);
        UserResponseDto response = userService.addUser(userRequestDto);
        log.info("Пользователь добавлен: {}", response);
        return response;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь удалён");
    }
}
