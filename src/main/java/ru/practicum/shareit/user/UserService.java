package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User save(User user) {
        User user1 = userStorage.save(user);
        log.debug(String.valueOf(LogMessages.ADD), user1);
        return user1;
    }

    public List<User> getAll() {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), userStorage.getALL());
        return userStorage.getALL();
    }

    public User getById(Long userId) {
        if (userStorage.getById(userId) == null)
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        log.debug(String.valueOf(LogMessages.GET), userStorage.getById(userId));
        return userStorage.getById(userId);
    }

    public User update(User user) {
        User user1 = userStorage.update(user);
        log.debug(String.valueOf(LogMessages.UPDATE), user1);
        return user1;
    }

    public User removeById(Long userId) {
        User user = userStorage.removeById(userId);
        log.debug(String.valueOf(LogMessages.REMOVE), user);
        return user;
    }
}