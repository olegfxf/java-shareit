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
    UserDLAStorage userDLAStorage;

    @Autowired
    public UserService(UserDLAStorage userDLAStorage) {
        this.userDLAStorage = userDLAStorage;
    }

    public User save(User user) {
        User user1 = userDLAStorage.save(user);
        log.debug(String.valueOf(LogMessages.ADD), user1);
        return user1;
    }

    public List<User> getAll() {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), userDLAStorage.getALL());
        return userDLAStorage.getALL();
    }

    public User getById(Long userId) {
        if (userDLAStorage.getById(userId) == null)
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        log.debug(String.valueOf(LogMessages.GET), userDLAStorage.getById(userId));
        return userDLAStorage.getById(userId);
    }


    public User update(User user) {
        User user1 = userDLAStorage.update(user);
        log.debug(String.valueOf(LogMessages.UPDATE), user1);
        return user1;
    }


    public User removeById(Long userId) {
        User user = userDLAStorage.getById(userId);
        userDLAStorage.removeById(userId);
        log.debug(String.valueOf(LogMessages.REMOVE), user);
        return user;
    }
}