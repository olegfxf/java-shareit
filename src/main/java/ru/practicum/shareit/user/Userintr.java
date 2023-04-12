package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface Userintr {
    List<User> getAll();
    User getById(Long userId);
    User update(User user);
    User removeById(Long userId);

}
