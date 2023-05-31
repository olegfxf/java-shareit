package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoReq;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractServiceImpl<User, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository,
                       UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public UserDtoRes updateCustomer(Long id, UserDtoReq userDtoReq) {
        if (!userRepository.existsById(id))
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        User user = getById(id);
        if (userDtoReq.getName() != null) user.setName(userDtoReq.getName());
        if (userDtoReq.getEmail() != null) user.setEmail(userDtoReq.getEmail());

        log.debug(String.valueOf(LogMessages.TRY_PATCH), userDtoReq);
        return UserMapper.toUserDtoRes(userRepository.save(user));
    }


    public UserDtoRes save1(User user) {
//        if (userRepository.existsUserByEmail(user.getEmail()))
//            throw new ValidationException(String.valueOf(HandlerMessages.CONFLICT));

        return new UserDtoRes(userRepository.save(user));
    }

    public UserDtoRes removeById1(Long userId) {
        User user = userRepository.findById(userId).get();
        userRepository.deleteById(userId);
        return new UserDtoRes(user);
    }

    public List<UserDtoRes> getAll1() {
        return userRepository.findAll().stream()
                .map(e -> UserMapper.toUserDtoRes(e)).collect(Collectors.toList());
    }

    public UserDtoRes getById1(Long userId) {
        return UserMapper.toUserDtoRes(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND))));
    }

    public UserDtoRes update1(User user) {
        if (!userRepository.findById(user.getId()).isPresent())
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        return UserMapper.toUserDtoRes(userRepository.save(user));
    }

}