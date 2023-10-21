package ru.practicum.service.user;

import ru.practicum.model.user.UserRequestDto;
import ru.practicum.model.user.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getUsersAdmin(List<Long> users, Integer from, Integer size);

    UserResponseDto addUser(UserRequestDto userRequestDto);

    void deleteUser(Long userId);

}
