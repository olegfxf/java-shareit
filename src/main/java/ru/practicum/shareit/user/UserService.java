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
public class UserService implements Userintr{
    UserDao userDao;
    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User save(User user) {
        log.debug(String.valueOf(LogMessages.ADD), user);
        return userDao.save(user);
    }

    public List<User> getAll() {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), userDao.getALL());
        return userDao.getALL();
    }

    public User getById(Long userId) {
        if (userDao.getById(userId) == null)
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);

        log.debug(String.valueOf(LogMessages.GET), userDao.getById(userId));
        return userDao.getById(userId);
    }


    public User update(User user) {

        log.debug(String.valueOf(LogMessages.UPDATE), user);
        return userDao.update(user);
    }


    public User removeById(Long userId) {
        User user = userDao.getById(userId);
        userDao.removeById(userId);

        log.debug(String.valueOf(LogMessages.REMOVE), userId);
        return user;
    }
}