package ru.practicum.service.user;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserRequestDto;
import ru.practicum.model.user.UserResponseDto;
import ru.practicum.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public List<UserResponseDto> getUsersAdmin(List<Long> users, Integer from, Integer size) {
        List<User> response;
        if (users == null) {
            response = userRepository.findAll(PageRequest.of(from / size, size)).toList();
        } else {
            response = userRepository.findByList(users, PageRequest.of(from / size, size)).toList();
        }
        return response.stream().map(user -> mapper.map(user, UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        User user = userRepository.save(mapper.map(userRequestDto, User.class));
        return mapper.map(user, UserResponseDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
    }
}
